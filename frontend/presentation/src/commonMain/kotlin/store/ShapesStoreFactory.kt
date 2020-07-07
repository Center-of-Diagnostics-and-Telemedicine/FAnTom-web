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
    ReaktiveExecutor<Intent, Nothing, State, Result, Nothing>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleSliceNumberChange -> dispatch(Result.SliceNumberChanged(intent.sliceNumber))
        is Intent.HandleExternalSliceNumberChanged ->
          handleExternalSliceNumberChanged(intent.sliceNumber, intent.cut, getState)
        is Intent.HandleMousePosition -> handleMousePosition(
          intent.dicomX,
          intent.dicomY,
          getState
        )
        is Intent.HandleMarks -> handleMarks(intent.list, getState)
      }.let {}
    }

    private fun handleMarks(list: List<MarkDomain>, state: () -> State) {
      dispatch(Result.Marks(list))
      updateCircles(list, state)
    }

    private fun handleExternalSliceNumberChanged(
      sliceNumber: Int,
      externalCut: Cut,
      getState: () -> State
    ) {
      when {
        cut.horizontalCutData.type == externalCut.type ->
          updateHorizontalLine(sliceNumber, externalCut)
        cut.verticalCutData.type == externalCut.type ->
          updateVerticalLine(sliceNumber, externalCut)
      }
      updateCircles(getState().marks, getState)
    }

    private fun updateCircles(list: List<MarkDomain>, state: () -> State) {
      val circles = list.mapNotNull { it.toCircle(cut, state().sliceNumber) }
      dispatch(Result.Circles(circles))
    }

    private fun updateVerticalLine(sliceNumber: Int, externalCut: Cut) {
      val coefficient = sliceNumber.toDouble() / externalCut.data!!.maxFramesSize
      dispatch(Result.VerticalLineChanged(coefficient))
    }

    private fun updateHorizontalLine(sliceNumber: Int, externalCut: Cut) {
      val coefficient = sliceNumber.toDouble() / externalCut.data!!.maxFramesSize
      dispatch(Result.HorizontalLineChanged(coefficient))
    }

    private fun handleMousePosition(
      dicomX: Double,
      dicomY: Double,
      getState: () -> State
    ) {
      println("x = $dicomX,y = $dicomY, sliceNumber = ${getState().sliceNumber} ")
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
