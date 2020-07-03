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
import model.Cut
import model.getPosition
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
          handleExternalSliceNumberChanged(intent.sliceNumber, intent.cut)
        is Intent.HandleMousePosition -> handleMousePosition(
          intent.dicomX,
          intent.dicomY,
          getState
        )
        is Intent.HandleMarks -> dispatch(Result.Marks(intent.list))
      }.let {}
    }

    private fun handleExternalSliceNumberChanged(
      sliceNumber: Int,
      externalCut: Cut
    ) {
      when {
        cut.horizontalCutData.type == externalCut.type ->
          updateHorizontalLine(sliceNumber, externalCut)
        cut.verticalCutData.type == externalCut.type ->
          updateVerticalLine(sliceNumber, externalCut)
      }
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
      hounsfield(dicomX, dicomY)
    }

    private fun hounsfield(position: Double, dicomY: Double) {
      singleFromCoroutine {
        repository.getHounsfieldData(position.toInt(), dicomY.toInt(), position.z.toInt())
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
