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
  val researchId: Int
) : ShapesStoreAbstractFactory(
  storeFactory = storeFactory,
  cut = cut,
  researchId = researchId
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

        is Intent.HandleCenterMarkClick -> handleCenterMarkClick(
          intent.dicomX,
          intent.dicomY,
          getState
        )

        is Intent.HandleMoveInClick -> handleMoveInClick(intent.deltaX, intent.deltaY, getState)

        is Intent.HandleStopMoving -> handleStopMoving(getState)

        is Intent.HandleStartClick ->
          handleStartClick(intent.startDicomX, intent.startDicomY, getState)
      }.let {}
    }

    private fun handleStartClick(
      startDicomX: Double,
      startDicomY: Double,
      getState: () -> State
    ) {
      val state = getState()
      val circles = state.circles
      val rects = getState().rects

      dispatch(Result.RectInMove(null))

      val moveRect = rects.firstOrNull {
        val partSideLength = it.sideLength / 2
        val inVerticalBound = startDicomY > it.top - partSideLength && startDicomY < it.top + partSideLength
        val inHorizontalBound = startDicomX > it.left - partSideLength && startDicomX < it.left + partSideLength
        inVerticalBound && inHorizontalBound
      }

      if (moveRect != null) {
        dispatch(Result.RectInMove(moveRect))
      } else {
        state.marks.firstOrNull { it.selected }?.let {
          publish(Label.UnselectMark(it))
        }

        val circle = circles.getCircleByPosition(dicomX = startDicomX, dicomY = startDicomY)
        circle?.let { circleToSelect ->
          state.marks.firstOrNull { it.id == circleToSelect.id }?.let {
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
            publish(Label.UpdateMark(it))
          }
        }
      } else {
        selectedMark?.let { markToUpdate ->
          cut.updateCoordinates(markToUpdate, deltaX, deltaY)?.let {
            publish(Label.UpdateMark(it))
          }
        }
      }
    }

    private fun handleCenterMarkClick(
      dicomX: Double,
      dicomY: Double,
      getState: () -> State
    ) {
      val state = getState()
      val circles = state.circles

      val circle = circles.getCircleByPosition(
        dicomX = dicomX,
        dicomY = dicomY
      )
      circle?.let {
        state.marks.firstOrNull { it.id == circle.id }?.let {
          publish(Label.SelectMark(it))
          publish(Label.CenterMark(it))
        }
      }
    }

    private fun handleMarks(list: List<MarkDomain>, state: () -> State) {
      dispatch(Result.Marks(list))
      updateCircles(list, state)
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
      updateCircles(getState().marks, getState)
    }

    private fun updateCircles(list: List<MarkDomain>, state: () -> State) {
      val circles = list.mapNotNull { it.toCircle(cut, state().sliceNumber) }
      val selectedCircle = circles.firstOrNull { it.highlight }
      val rectangles = if (selectedCircle != null && selectedCircle.isCenter) {
        selectedCircle.toRects(cut)
      } else {
        listOf()
      }
      dispatch(Result.Circles(circles))
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
          mipValue = 0
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
