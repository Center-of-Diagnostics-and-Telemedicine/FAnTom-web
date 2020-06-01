package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.Cut
import repository.ResearchRepository
import store.cut.ShapesStore.Intent
import store.cut.ShapesStore.State
import store.cut.ShapesStoreAbstractFactory

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
  override fun createExecutor(): Executor<Intent, Nothing, State, Result, Nothing> = ExecutorImpl()

  private inner class ExecutorImpl :
    ReaktiveExecutor<Intent, Nothing, State, Result, Nothing>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleSliceNumberChange -> dispatch(Result.SliceNumberChanged(intent.sliceNumber))
        is Intent.HandleExternalSliceNumberChanged ->
          handleExternalSliceNumberChanged(intent.sliceNumber, intent.cut)
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

    private fun updateHorizontalLine(
      sliceNumber: Int,
      externalCut: Cut
    ) {
      val coefficient = sliceNumber.toDouble() / externalCut.data!!.maxFramesSize
      dispatch(Result.HorizontalLineChanged(coefficient))
    }

    private fun updateVerticalLine(
      sliceNumber: Int,
      externalCut: Cut
    ) {
      println("sliceNumber = $sliceNumber")
      println("external -> externalCut = ${externalCut.type.intType}, externalMaxFrameSize = ${externalCut.data!!.maxFramesSize}, externalHeight = ${externalCut.data!!.height}")
      println("cut -> cut = ${cut.type.intType}, maxFramesSize = ${cut.data!!.maxFramesSize}, height = ${cut.data!!.height}")
      val coefficient = sliceNumber.toDouble() / externalCut.data!!.maxFramesSize
      println("coefficient = $coefficient cut = ${cut.type.intType}")
      dispatch(Result.VerticalLineChanged(coefficient))
    }
  }

}
