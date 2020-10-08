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
  val data: ResearchSlicesSizesDataNew
) : CutsContainerStoreAbstractFactory(
  storeFactory = storeFactory,
  data = data
) {

  override fun createExecutor(): Executor<Intent, Unit, State, Result, Nothing> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Nothing>() {
    override fun executeAction(action: Unit, getState: () -> State) {
      singleFromFunction {
        val grid = when(data.type){
          ResearchType.CT,
          ResearchType.MG -> initialFourGrid(data.type)
          ResearchType.DX -> initialSingleGrid(data.type)
        }
        Result.Loaded(items = grid.buildCuts(data), grid = grid)
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
        is Intent.HandleGridChanged -> handleGridChanged(intent)
        is Intent.HandleChangeCutType -> handleChangeCutType(getState, intent.cut, intent.cutType)
      }.let {}
    }

    private fun handleChangeCutType(getState: () -> State, oldCut: Cut, newCutType: CutType) {
      val items = getState().grid.updateCuts(data = data, oldCut = oldCut, newCutType = newCutType)
      dispatch(Result.Loaded(items = items, grid = getState().grid))
    }

    private fun handleGridChanged(intent: Intent.HandleGridChanged) {
      dispatch(Result.Loaded(items = intent.grid.buildCuts(data), grid = intent.grid))
    }
  }

}
