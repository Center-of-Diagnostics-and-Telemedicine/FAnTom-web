package components.shapes

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.*
import store.shapes.ShapesStore
import store.shapes.ShapesStore.*

internal class ShapesStoreProvider(
  private val storeFactory: StoreFactory,
  private val researchId: Int,
  private val cutType: CutType
) {

  fun provide(): ShapesStore =
    object : ShapesStore, Store<Intent, State, Label> by storeFactory.create(
      name = "ShapesStore_${researchId}_${cutType.intType}",
      initialState = State(
        horizontalCoefficient = 0.5,
        verticalCoefficient = 0.5,
        sliceNumber = 1,
        position = null,
        shapes = listOf(),
        hounsfield = null,
        marks = listOf(),
        expertMarks = listOf(),
        rects = listOf(),
        moveRect = null,
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
    data class SliceNumberChanged(val sliceNumber: Int) : Result()
    data class HorizontalCoefficientChanged(val coefficient: Double) : Result()
    data class VerticalCoefficientChanged(val coefficient: Double) : Result()
    data class PointPositionChanged(val position: PointPosition?) : Result()
    data class HounsfieldChanged(val hu: Double) : Result()
    data class Marks(val marks: List<MarkModel>) : Result()
    data class ExpertMarks(val expertMarks: List<MarkModel>) : Result()
    data class Shapes(val shapes: List<Shape>) : Result()
    data class Rects(val rects: List<Rect>) : Result()
    data class SideRectInMove(val moveRect: Rect?) : Result()
  }

  private inner class ExecutorImpl :
    ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleSliceNumberChange -> dispatch(
          Result.SliceNumberChanged(
            intent.sliceNumber
          )
        )

        is Intent.HandleExternalSliceNumberChanged ->
          handleExternalSliceNumberChanged(intent.sliceNumber, intent.cut, getState)

        is Intent.HandleMousePosition ->
          handleMousePosition(intent.dicomX, intent.dicomY, getState)

        is Intent.HandleMarks -> handleMarks(intent.list, getState)

        is Intent.HandleExpertMarks -> handleExpertMarks(intent.list, getState)

        is Intent.HandleMoveInClick -> handleMoveInClick(intent.deltaX, intent.deltaY, getState)

        is Intent.HandleStopMoving -> handleStopMoving(getState)

        is Intent.HandleStartClick ->
          handleStartClick(intent.startDicomX, intent.startDicomY, getState)

        is Intent.HandleChangeCutType -> publish(Label.ChangeCutType(intent.value))
      }.let {}
    }

    private fun handleStartClick(
      startDicomX: Double,
      startDicomY: Double,
      getState: () -> State
    ) {
      val state = getState()
      val shapes = state.shapes
      val sideRects = getState().rects
      val marks = state.marks.plus(state.expertMarks)

      dispatch(Result.SideRectInMove(null))

      val moveRect = sideRects.firstOrNull {
        val partSideLength = it.sideLength / 2
        val inVerticalBound =
          startDicomY > it.top - partSideLength && startDicomY < it.top + partSideLength
        val inHorizontalBound =
          startDicomX > it.left - partSideLength && startDicomX < it.left + partSideLength
        inVerticalBound && inHorizontalBound
      }

      if (moveRect != null) {
        dispatch(Result.SideRectInMove(moveRect))
      } else {
        marks.firstOrNull { it.selected }?.let {
          publish(Label.UnselectMark(it))
        }

        val shape = shapes.getShapeByPosition(dicomX = startDicomX, dicomY = startDicomY)
        if (shape != null) {
          marks.firstOrNull { it.id == shape.id }?.let {
            publish(Label.SelectMark(it))
          }
        }
      }
    }

    private fun handleMoveInClick(deltaX: Double, deltaY: Double, getState: () -> State) {
      val selectedMark = getState().marks.firstOrNull { it.selected }
      val moveRect = getState().moveRect

      if (moveRect != null) {
        selectedMark?.let { markToUpdate ->
//          cut.updateCoordinatesByRect(markToUpdate, deltaX, deltaY, moveRect)?.let {
//            publish(Label.UpdateMarkCoordinates(it))
//          }
        }
      } else {
        selectedMark?.let { markToUpdate ->
//          cut.updateCoordinates(markToUpdate, deltaX, deltaY)?.let {
//            publish(Label.UpdateMarkCoordinates(it))
//          }
        }
      }
    }

    private fun handleMarks(list: List<MarkModel>, state: () -> State) {
      dispatch(Result.Marks(list))
      updateShapes(state().expertMarks.plus(list), state().sliceNumber)
    }

    private fun handleExpertMarks(
      list: List<ExpertQuestionsModel>,
      state: () -> State
    ) {
      val models = list
//        .filter { it.expertMarkEntity.cutType == cut.type.intType }
        .map(ExpertQuestionsModel::toMarkModel)
      dispatch(Result.ExpertMarks(models))
      updateShapes(state().marks.plus(models), state().sliceNumber)
    }

    private fun handleStopMoving(getState: () -> State) {
      getState().marks.firstOrNull { it.selected }?.let { markToUpdate ->
        publish(Label.UpdateMarkWithSave(markToUpdate))
      }
    }

    private fun handleExternalSliceNumberChanged(
      sliceNumber: Int,
      externalCut: Cut,
      getState: () -> State
    ) {
//      when {
//        cut.verticalCutData?.type == externalCut.type ->
//          updateVerticalCoefficient(sliceNumber, externalCut)
//        cut.horizontalCutData?.type == externalCut.type ->
//          updateHorizontalCoefficient(sliceNumber, externalCut)
//      }
      updateShapes(getState().marks.plus(getState().expertMarks), getState().sliceNumber)
    }

    private fun updateShapes(marks: List<MarkModel>, sliceNumber: Int) {
//      val shapes = marks
//        .filter { it.visible }
//        .mapNotNull { it.toShape(cut, sliceNumber) }
//      val selectedShape = shapes.firstOrNull { it.highlight }
//      val rectangles =
//        if (selectedShape != null && selectedShape.isCenter && selectedShape.editable) {
//          selectedShape.toRects(cut)
//        } else {
//          listOf()
//        }
//      dispatch(Result.Shapes(shapes))
//      dispatch(Result.Rects(rectangles))
    }

    private fun updateHorizontalCoefficient(sliceNumber: Int, externalCut: Cut) {
      val coefficient = sliceNumber.toDouble() / externalCut.data.n_images
      dispatch(Result.HorizontalCoefficientChanged(coefficient))
    }

    private fun updateVerticalCoefficient(sliceNumber: Int, externalCut: Cut) {
      val coefficient = sliceNumber.toDouble() / externalCut.data.n_images
      dispatch(Result.VerticalCoefficientChanged(coefficient))
    }

    private fun handleMousePosition(
      dicomX: Double,
      dicomY: Double,
      getState: () -> State
    ) {
//      val position = cut.getPosition(
//        dicomX = dicomX,
//        dicomY = dicomY,
//        sliceNumber = getState().sliceNumber
//      )
//      println("mouse position = $position")
//      dispatch(Result.PointPositionChanged(position = position))
//      if (position != null) {
//        hounsfield(dicomX, dicomY, getState)
//      }
    }

    private fun hounsfield(
      dicomX: Double,
      dicomY: Double,
      state: () -> State
    ) {
//      singleFromCoroutine {
//        repository.getHounsfieldData(
//          horizontal = dicomX.toInt(),
//          vertical = dicomY.toInt(),
//          sliceNumber = state().sliceNumber,
//          type = cut.type.intType,
//          mipMethod = MIP_METHOD_TYPE_NO_MIP,
//          mipValue = 0,
//          width = cut.data.screen_size_h,
//          height = 0
//        )
//      }
//        .subscribeOn(ioScheduler)
//        .map(Result::HounsfieldChanged)
//        .observeOn(mainScheduler)
//        .subscribeScoped(
//          isThreadLocal = true,
//          onSuccess = ::dispatch
//        )
    }
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.SliceNumberChanged -> copy(sliceNumber = result.sliceNumber)
        is Result.HorizontalCoefficientChanged -> copy(horizontalCoefficient = result.coefficient)
        is Result.VerticalCoefficientChanged -> copy(verticalCoefficient = result.coefficient)
        is Result.PointPositionChanged -> copy(position = result.position)
        is Result.Shapes -> copy(shapes = result.shapes)
        is Result.Rects -> copy(rects = result.rects)
        is Result.HounsfieldChanged -> copy(hounsfield = result.hu.toInt())
        is Result.Marks -> copy(marks = result.marks)
        is Result.ExpertMarks -> copy(expertMarks = result.expertMarks)
        is Result.SideRectInMove -> copy(moveRect = result.moveRect)
      }
  }
}