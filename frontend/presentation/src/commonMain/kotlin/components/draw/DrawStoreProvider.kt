package components.draw

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.CutType
import store.draw.DrawStore
import store.draw.DrawStore.*
import kotlin.math.pow
import kotlin.math.sqrt

internal class DrawStoreProvider(
  private val storeFactory: StoreFactory,
  private val researchId: Int,
  private val cutType: CutType
) {

  fun provide(): DrawStore =
    object : DrawStore, Store<Intent, State, Label> by storeFactory.create(
      name = "DrawStore_${researchId}_${cutType.intType}",
      initialState = State(
        startDicomX = 0.0,
        startDicomY = 0.0,
        dicomRadiusHorizontal = 0.0,
        dicomRadiusVertical = 0.0,
        isDrawingEllipse = false,
        isMoving = false,
        isContrastBrightness = false,
        cutType = cutType,
      ),
//      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  private sealed class Result : JvmSerializable {
    data class StartDrawEllipse(val startDicomX: Double, val startDicomY: Double) : Result()
    data class StartDrawRectangle(val startDicomX: Double, val startDicomY: Double) : Result()
    data class MultiPlanarDrawing(val newDicomX: Double, val newDicomY: Double) : Result()
    data class ExternalDrawing(val dicomX: Double, val dicomY: Double) : Result()
    data class StartContrastBrightness(val startDicomX: Double, val startDicomY: Double) : Result()
    data class ContrastBrightness(val dicomX: Double, val dicomY: Double) : Result()
    data class StartClick(val startDicomX: Double, val startDicomY: Double) : Result()
    data class MouseMove(val dicomX: Double, val dicomY: Double) : Result()
    data class MouseMoveInClick(val dicomX: Double, val dicomY: Double) : Result()
    data class PlanarDrawing(val dicomX: Double, val dicomY: Double) : Result()

    object Idle : Result()
  }

  private inner class ExecutorImpl :
    ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.StartDrawEllipse -> {
//          if (research.category != DOSE_REPORT_RESEARCH_CATEGORY) {
          handleStartDrawEllipse(intent.startDicomX, intent.startDicomY)
//          } else null
        }
        is Intent.StartDrawRectangle -> handleStartDrawRectangle(
          intent.startDicomX,
          intent.startDicomY
        )
        is Intent.StartContrastBrightness ->
          handleStartContrastBrightness(intent.startDicomX, intent.startDicomY)
        is Intent.StartMouseClick -> handleStartClick(intent.startDicomX, intent.startDicomY)
        is Intent.Move -> handleMove(intent.dicomX, intent.dicomY, getState)

        is Intent.MouseUp -> handleMouseUp(getState)
        is Intent.MouseOut -> handleMouseOut()
        is Intent.MouseWheel -> handleMouseData(intent)
        Intent.DoubleClick -> publish(Label.OpenFullCut)
      }.let {}
    }

    private fun handleMouseData(intent: Intent.MouseWheel) =
//      if (cut.data.n_images > 1) {
//        publish(Label.ChangeSlice(intent.deltaDicomY))
//      } else
      null

    private fun handleStartClick(startDicomX: Double, startDicomY: Double) {
      dispatch(Result.StartClick(startDicomX = startDicomX, startDicomY = startDicomY))
      publish(Label.StartClick(startDicomX = startDicomX, startDicomY = startDicomY))
    }

    private fun handleStartContrastBrightness(startDicomX: Double, startDicomY: Double) {
      dispatch(Result.StartContrastBrightness(startDicomX = startDicomX, startDicomY = startDicomY))
    }

    fun handleStartDrawEllipse(startDicomX: Double, startDicomY: Double) {
      dispatch(Result.StartDrawEllipse(startDicomX = startDicomX, startDicomY = startDicomY))
    }

    fun handleStartDrawRectangle(startDicomX: Double, startDicomY: Double) {
      dispatch(Result.StartDrawRectangle(startDicomX = startDicomX, startDicomY = startDicomY))
    }

    private fun handleMouseOut() {
      dispatch(Result.Idle)
      publish(Label.MouseMove(-1.0, -1.0))
    }

    private fun handleMouseUp(getState: () -> State) {
      val state = getState()
      when {
        state.isDrawingEllipse -> {
          dispatch(Result.Idle)
//          val circle = state.circle(cut.isPlanar())
//          publish(Label.CircleDrawn(circle = circle))
        }
        state.isDrawingRectangle -> {
          dispatch(Result.Idle)
          val rectangle = state.rectangle()
          publish(Label.RectangleDrawn(rectangle = rectangle))
        }
        state.isContrastBrightness -> {
          dispatch(Result.Idle)
          publish(Label.ContrastBrightnessChanged)
        }
        state.isMoving -> {
          dispatch(Result.Idle)
          publish(Label.StopMove)
        }
      }
    }

    private fun handleMove(dicomX: Double, dicomY: Double, getState: () -> State) {
      val state = getState()
      when {
        state.isDrawingEllipse -> {
//          if (cut.isPlanar()) {
//            dispatch(Result.PlanarDrawing(dicomX, dicomY))
//          } else {
          dispatch(Result.MultiPlanarDrawing(dicomX, dicomY))
//          }
        }
        state.isDrawingRectangle -> {
          dispatch(Result.PlanarDrawing(dicomX, dicomY))
        }
        state.isContrastBrightness -> {
          dispatch(Result.ContrastBrightness(dicomX, dicomY))
          val deltaX = dicomX - state.startDicomX
          val deltaY = dicomY - state.startDicomY
          publish(Label.ChangeContrastBrightness(deltaX = deltaX, deltaY = deltaY))
        }
        state.isMoving -> {
          dispatch(Result.MouseMoveInClick(dicomX, dicomY))
          val deltaX = dicomX - state.startDicomX
          val deltaY = dicomY - state.startDicomY
          publish(Label.MoveInClick(deltaX = deltaX, deltaY = deltaY))

        }
        else -> {
          dispatch(Result.MouseMove(dicomX, dicomY))
          publish(Label.MouseMove(dicomX, dicomY))
        }
      }
    }
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.StartDrawEllipse -> copy(
          startDicomX = result.startDicomX,
          startDicomY = result.startDicomY,
          isDrawingEllipse = true
        )
        is Result.StartDrawRectangle -> copy(
          startDicomX = result.startDicomX,
          startDicomY = result.startDicomY,
          isDrawingRectangle = true
        )
        is Result.StartContrastBrightness -> copy(
          startDicomX = result.startDicomX,
          startDicomY = result.startDicomY,
          isContrastBrightness = true
        )
        is Result.StartClick -> copy(
          isMoving = true,
          startDicomX = result.startDicomX,
          startDicomY = result.startDicomY
        )
        is Result.MultiPlanarDrawing -> {
          val xSqr = result.newDicomX - startDicomX
          val ySqr = result.newDicomY - startDicomY
          val radius = sqrt((xSqr).pow(2) + (ySqr).pow(2))
          copy(dicomRadiusHorizontal = radius, dicomRadiusVertical = radius)
        }
        is Result.PlanarDrawing -> copy(
          dicomRadiusHorizontal = (result.dicomX - startDicomX),
          dicomRadiusVertical = (result.dicomY - startDicomY)
        )
        is Result.ContrastBrightness -> copy(
          startDicomX = result.dicomX,
          startDicomY = result.dicomY
        )
        is Result.MouseMove -> copy(
          startDicomX = result.dicomX - startDicomX,
          startDicomY = result.dicomY - startDicomY
        )
        is Result.MouseMoveInClick -> copy(
          startDicomX = result.dicomX,
          startDicomY = result.dicomY
        )
        Result.Idle -> copy(
          startDicomX = 0.0,
          startDicomY = 0.0,
          dicomRadiusHorizontal = 0.0,
          dicomRadiusVertical = 0.0,
          isDrawingEllipse = false,
          isDrawingRectangle = false,
          isContrastBrightness = false,
          isMoving = false
        )
        is Result.ExternalDrawing -> {
          val radius = sqrt((result.dicomX).pow(2) + (result.dicomY).pow(2))
          copy(dicomRadiusHorizontal = radius, dicomRadiusVertical = radius)
        }
      }
  }
}