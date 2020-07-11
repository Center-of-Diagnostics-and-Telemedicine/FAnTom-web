package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.singleFromFunction
import com.badoo.reaktive.single.subscribeOn
import model.*
import store.gridcontainer.CutsContainerStore.Intent
import store.gridcontainer.CutsContainerStore.State
import store.gridcontainer.CutsContainerStoreAbstractFactory

internal class CutsContainerStoreFactory(
  storeFactory: StoreFactory,
  data: ResearchSlicesSizesData
) : CutsContainerStoreAbstractFactory(
  storeFactory = storeFactory
) {

  private val axialCutData = CutData(CutType.Axial, data.axial, axialColor)
  private val frontalCutData = CutData(
    CutType.Frontal,
    data.frontal,
    frontalColor
  )
  private val sagittalCutData = CutData(
    CutType.Sagittal,
    data.sagittal,
    sagittalColor
  )
  private val axialCut = Cut(
    type = CutType.Axial,
    data = data.axial,
    color = axialColor,
    horizontalCutData = frontalCutData,
    verticalCutData = sagittalCutData
  )
  private val frontalCut = Cut(
    type = CutType.Frontal,
    data = data.frontal,
    color = frontalColor,
    horizontalCutData = axialCutData,
    verticalCutData = sagittalCutData
  )
  private val sagittalCut = Cut(
    type = CutType.Sagittal,
    data = data.sagittal,
    color = sagittalColor,
    horizontalCutData = axialCutData,
    verticalCutData = frontalCutData
  )

  override fun createExecutor(): Executor<Intent, Unit, State, Result, Nothing> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Nothing>() {
    override fun executeAction(action: Unit, getState: () -> State) {
      val types = listOf(CutType.Axial, CutType.Empty, CutType.Frontal, CutType.Sagittal)
      singleFromFunction {
        Result.Loaded(
          items = buildCuts(types),
          grid = initialFourGrid()
        )
      }
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribeScoped(
          isThreadLocal = true,
          onSuccess = ::dispatch
        )
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleGridChanged ->
          dispatch(Result.Loaded(items = buildCuts(intent.grid.types), grid = intent.grid))
      }.let {}
    }

    private fun buildCuts(types: List<CutType>): List<Cut> =
      types.map {
        buildCut(it)
      }

    private fun buildCut(type: CutType): Cut {
      return when (type) {
        CutType.Frontal -> frontalCut
        CutType.Sagittal -> sagittalCut
        CutType.Empty,
        CutType.Axial
        -> axialCut
      }
    }
  }

}
