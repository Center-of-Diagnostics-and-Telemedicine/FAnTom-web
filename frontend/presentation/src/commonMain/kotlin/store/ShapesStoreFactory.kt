package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.subscribeOn
import model.*
import repository.ResearchRepository
import store.shapes.ShapesStore.*
import store.shapes.ShapesStoreAbstractFactory

internal class ShapesStoreFactory(
  storeFactory: StoreFactory,
  val cut: Cut,
  val repository: ResearchRepository,
  val research: Research
) : ShapesStoreAbstractFactory(
  storeFactory = storeFactory,
  cut = cut,
  research = research
) {
  override fun createExecutor(): Executor<Intent, Nothing, State, Result, Label> = ExecutorImpl()

  private inner class ExecutorImpl :
    ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleSliceNumberChange -> dispatch(Result.SliceNumberChanged(intent.sliceNumber))

        is Intent.HandleExternalSliceNumberChanged ->
          handleExternalSliceNumberChanged(intent.sliceNumber, intent.cut, getState)

        is Intent.HandleMousePosition ->
          handleMousePosition(intent.dicomX, intent.dicomY, getState)

        is Intent.HandleMarks -> handleMarks(intent.list, getState)

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
        state.marks.firstOrNull { it.selected }?.let {
          publish(Label.UnselectMark(it))
        }

        val circle = shapes.getShapeByPosition(dicomX = startDicomX, dicomY = startDicomY)
        if (circle != null) {
          state.marks.firstOrNull { it.id == circle.id }?.let {
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
          cut.updateCoordinatesByRect(markToUpdate, deltaX, deltaY, moveRect)?.let {
            publish(Label.UpdateMarkCoordinates(it))
          }
        }
      } else {
        selectedMark?.let { markToUpdate ->
          cut.updateCoordinates(markToUpdate, deltaX, deltaY)?.let {
            publish(Label.UpdateMarkCoordinates(it))
          }
        }
      }
    }

    private fun handleMarks(list: List<MarkModel>, state: () -> State) {
      dispatch(Result.Marks(list))
      updateShapes(list, state)
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
      when {
        cut.verticalCutData?.type == externalCut.type ->
          updateVerticalCoefficient(sliceNumber, externalCut)
        cut.horizontalCutData?.type == externalCut.type ->
          updateHorizontalCoefficient(sliceNumber, externalCut)
      }
      updateShapes(getState().marks, getState)
    }

    private fun updateShapes(list: List<MarkModel>, state: () -> State) {
      val shapes = list
        .filter { it.visible }
        .mapNotNull { it.toShape(cut, state().sliceNumber) }
      val selectedShape = shapes.firstOrNull { it.highlight }
      val rectangles = if (selectedShape != null && selectedShape.isCenter) {
        selectedShape.toRects(cut)
      } else {
        listOf()
      }
      dispatch(Result.Shapes(shapes))
      dispatch(Result.Rects(rectangles))
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
      val position = cut.getPosition(
        dicomX = dicomX,
        dicomY = dicomY,
        sliceNumber = getState().sliceNumber
      )
      dispatch(Result.PointPositionChanged(position = position))
      if (position != null) {
        hounsfield(dicomX, dicomY, getState)
      }
    }

    private fun hounsfield(
      dicomX: Double,
      dicomY: Double,
      state: () -> State
    ) {
      singleFromCoroutine {
        repository.getHounsfieldData(
          horizontal = dicomX.toInt(),
          vertical = dicomY.toInt(),
          sliceNumber = state().sliceNumber,
          type = cut.type.intType,
          mipMethod = MIP_METHOD_TYPE_NO_MIP,
          mipValue = 0,
          width = cut.data.screen_size_h,
          height = 0
        )
      }
        .subscribeOn(ioScheduler)
        .map(Result::HounsfieldChanged)
        .observeOn(mainScheduler)
        .subscribeScoped(
          isThreadLocal = true,
          onSuccess = ::dispatch
        )
    }
  }

}
