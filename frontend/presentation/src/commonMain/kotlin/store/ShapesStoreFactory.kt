package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.*
import repository.ResearchRepository
import store.shapes.ShapesStore.Intent
import store.shapes.ShapesStore.State
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
  override fun createExecutor(): Executor<Intent, Nothing, State, Result, Nothing> = ExecutorImpl()

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
          intent.cutType,
          getState
        )
        is Intent.HandleDrawing -> handleDrawing(intent.circle, intent.cutType)
      }.let {}
    }

    private fun handleDrawing(circle: Circle, externalCutType: CutType) {
      cut.buildCircle(circle, externalCutType)
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

    private fun updateVerticalLine(
      sliceNumber: Int,
      externalCut: Cut
    ) {
      val coefficient = sliceNumber.toDouble() / externalCut.data!!.maxFramesSize
      dispatch(Result.VerticalLineChanged(coefficient))
    }

    private fun handleMousePosition(
      dicomX: Double,
      dicomY: Double,
      cutType: CutType,
      getState: () -> State
    ) {
      if (cut.type == cutType) {
        println("x = $dicomX,y = $dicomY, sliceNumber = ${getState().sliceNumber} ")
        val position = cut.getPosition(
          dicomX = dicomX,
          dicomY = dicomY,
          sliceNumber = getState().sliceNumber
        )
        dispatch(Result.PointPositionChanged(position = position))
      }
    }

    private fun updateHorizontalLine(
      sliceNumber: Int,
      externalCut: Cut
    ) {
      val coefficient = sliceNumber.toDouble() / externalCut.data!!.maxFramesSize
      dispatch(Result.HorizontalLineChanged(coefficient))
    }
  }

}
