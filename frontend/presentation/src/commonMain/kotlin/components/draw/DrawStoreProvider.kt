package components.draw

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.observeOn
import com.badoo.reaktive.completable.subscribeOn
import com.badoo.reaktive.coroutinesinterop.completableFromCoroutine
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.*
import repository.MyMarksRepository
import store.draw.MyDrawStore
import store.draw.MyDrawStore.*

internal class DrawStoreProvider(
  private val storeFactory: StoreFactory,
  private val researchId: Int,
  private val plane: Plane,
  private val marksRepository: MyMarksRepository
) {

  fun provide(): MyDrawStore =
    object : MyDrawStore, Store<Intent, State, Label> by storeFactory.create(
      name = "MyDrawStore_${researchId}_${plane.type.intType}",
      initialState = State(
        cutType = plane.type,
        plane = plane,
        screenDimensionsModel = initialScreenDimensionsModel()
      ),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  private inner class ExecutorImpl :
    ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.StartDrawEllipse -> handleStartDrawEllipse(intent.startDicomX, intent.startDicomY)
        is Intent.StartDrawCircle -> handleStartDrawCircle(intent.startDicomX, intent.startDicomY)
        is Intent.StartDrawRectangle ->
          handleStartDrawRectangle(intent.startDicomX, intent.startDicomY)
        is Intent.StartContrastBrightness ->
          handleStartContrastBrightness(intent.startDicomX, intent.startDicomY)
        is Intent.StartMouseClick -> handleStartClick(intent.startDicomX, intent.startDicomY)

        is Intent.Move -> handleMove(intent.dicomX, intent.dicomY, getState())
        is Intent.MouseUp -> handleMouseUp(getState())
        is Intent.MouseOut -> handleMouseOut()
        is Intent.MouseWheel -> handleMouseWheel(intent.deltaDicomY)
        Intent.DoubleClick -> publish(Label.OpenFullCut)
        is Intent.UpdateScreenDimensions ->
          dispatch(Result.ScreenDimensionsChanged(intent.dimensions))
      }.let {}
    }

    private fun handleStartDrawEllipse(startDicomX: Double, startDicomY: Double) {
      dispatch(Result.StartDrawEllipse(ellipse = EllipseModel(startDicomX, startDicomY)))
    }

    private fun handleStartDrawCircle(startDicomX: Double, startDicomY: Double) {
      dispatch(Result.StartDrawCircle(circle = CircleModel(startDicomX, startDicomY)))
    }

    fun handleStartDrawRectangle(startDicomX: Double, startDicomY: Double) {
      dispatch(Result.StartDrawRectangle(rectangle = RectangleModel(startDicomX, startDicomY)))
    }

    private fun handleStartContrastBrightness(startDicomX: Double, startDicomY: Double) {
      val contrastBrightness = MouseClickPositionModel(startX = startDicomX, startY = startDicomY)
      dispatch(Result.StartContrastBrightness(contrastBrightness = contrastBrightness))
    }

    private fun handleStartClick(startDicomX: Double, startDicomY: Double) {
      completableFromCoroutine {
        val mouseInClickPosition =
          MouseClickPositionModel(startX = startDicomX, startY = startDicomY)
        dispatch(Result.MouseInClickPosition(mouseInClickPosition))
        marksRepository.setMarkByCoordinates(startDicomX, startDicomY)
      }
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribeScoped()
    }

    private fun handleMove(dicomX: Double, dicomY: Double, state: State) {
      val isShapeDrawing = state.shape != null
      val isContrastBrightness = state.contrastBrightness != null
      val isMouseMove = state.mousePosition != null
      val isMouseMoveInClick = state.mouseInClickPosition != null
      when {
        isShapeDrawing -> handleDrawing(dicomX, dicomY, state.shape!!)
        isContrastBrightness ->
          handleContrastBrightness(dicomX, dicomY, state.contrastBrightness!!)
        isMouseMoveInClick -> handleMouseMoveInClick(dicomX, dicomY, state.mouseInClickPosition!!)
        isMouseMove -> handleMouseMove(dicomX, dicomY)
      }
    }

    private fun handleMouseOut() {
      dispatch(Result.Idle)
    }

    private fun handleMouseUp(state: State) {
      val shape = state.shape
      when {
        shape != null -> {
          when (shape) {
            is RectangleModel -> publish(Label.RectangleDrawn(shape))
            is CircleModel -> publish(Label.CircleDrawn(shape))
            is EllipseModel -> publish(Label.EllipseDrawn(shape))
          }
        }
      }
      dispatch(Result.Idle)
    }

    private fun handleMouseMoveInClick(
      dicomX: Double,
      dicomY: Double,
      mouseInClickPosition: MouseClickPositionModel
    ) {
      dispatch(Result.MouseInClickPosition(mouseInClickPosition.copy(x = dicomX, y = dicomY)))
    }

    private fun handleMouseMove(dicomX: Double, dicomY: Double) {
      val mousePosition = PointPositionModel(x = dicomX, y = dicomY)
      dispatch(Result.MousePosition(mousePosition))
      publish(Label.MousePointPosition(mousePosition))
    }

    private fun handleContrastBrightness(
      dicomX: Double,
      dicomY: Double,
      contrastBrightnessModel: MouseClickPositionModel
    ) {
    }

    private fun handleDrawing(dicomX: Double, dicomY: Double, shape: Shape) {
      when (shape) {
        is RectangleModel -> handleRectangleDrawing(shape, dicomX, dicomY)
        is CircleModel -> handleCircleDrawing(shape, dicomX, dicomY)
        is EllipseModel -> handleEllipseDrawing(shape, dicomX, dicomY)
        else -> throw NotImplementedError("Not implemented shape type $shape")
      }
    }

    private fun handleEllipseDrawing(
      shape: EllipseModel,
      dicomX: Double,
      dicomY: Double
    ) {
      val ellipse = shape.copy(
        dicomWidth = dicomX - shape.dicomX,
        dicomHeight = dicomY - shape.dicomY
      )
      dispatch(Result.Ellipse(ellipse))
    }

    private fun handleCircleDrawing(shape: CircleModel, dicomX: Double, dicomY: Double) {
      val circle = shape.copy(
        dicomWidth = dicomX - shape.dicomX,
        dicomHeight = dicomY - shape.dicomY
      )
      dispatch(Result.Circle(circle))
    }

    private fun handleRectangleDrawing(
      shape: RectangleModel,
      dicomX: Double,
      dicomY: Double
    ) {
      val rectangle = shape.copy(
        dicomWidth = dicomX - shape.dicomX,
        dicomHeight = dicomY - shape.dicomY
      )
      dispatch(Result.Rectangle(rectangle))
    }

    private fun handleMouseWheel(deltaDicomY: Int) {}
  }

  private sealed class Result : JvmSerializable {
    data class StartDrawEllipse(val ellipse: EllipseModel) : Result()
    data class Ellipse(val ellipse: EllipseModel) : Result()

    data class StartDrawCircle(val circle: CircleModel) : Result()
    data class Circle(val circle: CircleModel) : Result()

    data class StartDrawRectangle(val rectangle: RectangleModel) : Result()
    data class Rectangle(val rectangle: RectangleModel) : Result()

    data class StartContrastBrightness(val contrastBrightness: MouseClickPositionModel) : Result()

    data class MouseInClickPosition(val mouseInClickPosition: MouseClickPositionModel) : Result()
    data class MousePosition(val mousePosition: PointPositionModel) : Result()
    data class ScreenDimensionsChanged(val dimensions: ScreenDimensionsModel) : Result()

    object Idle : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.StartDrawEllipse -> copy(shape = result.ellipse)
        is Result.Ellipse -> copy(shape = result.ellipse)

        is Result.StartDrawCircle -> copy(shape = result.circle)
        is Result.Circle -> copy(shape = result.circle)

        is Result.StartDrawRectangle -> copy(shape = result.rectangle)
        is Result.Rectangle -> copy(shape = result.rectangle)

        is Result.StartContrastBrightness -> copy(contrastBrightness = result.contrastBrightness)
        is Result.MouseInClickPosition -> copy(mouseInClickPosition = result.mouseInClickPosition)
        is Result.MousePosition -> copy(mousePosition = result.mousePosition)
        is Result.ScreenDimensionsChanged -> copy(screenDimensionsModel = result.dimensions)
        Result.Idle -> copy(
          shape = null,
          contrastBrightness = null,
          mousePosition = null,
          mouseInClickPosition = null
        )
      }
  }
}