package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.singleFromFunction
import com.badoo.reaktive.single.subscribeOn
import model.Cut
import model.CutType
import model.ResearchSlicesSizesData
import model.initialFourGrid
import store.gridcontainer.GridContainerStore.Intent
import store.gridcontainer.GridContainerStore.State
import store.gridcontainer.GridContainerStoreAbstractFactory

internal class GridContainerStoreFactory(
  storeFactory: StoreFactory,
  private val data: ResearchSlicesSizesData
) : GridContainerStoreAbstractFactory(
  storeFactory = storeFactory
) {
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
        is Intent.HandleGridChanged -> dispatch(
          Result.Loaded(
            items = buildCuts(intent.grid.types),
            grid = intent.grid
          )
        )
      }.let {}
    }

    private fun buildCuts(types: List<CutType>): List<Cut> =
      types.map {
        buildCut(it)
      }

    private fun buildCut(type: CutType): Cut = when (type) {
      CutType.Frontal -> Cut(type = type, data = data.frontal)
      CutType.Sagittal -> Cut(type = type, data = data.sagittal)
      CutType.Empty,
      CutType.Axial
      -> Cut(type = type, data = data.axial)
    }
  }

}
