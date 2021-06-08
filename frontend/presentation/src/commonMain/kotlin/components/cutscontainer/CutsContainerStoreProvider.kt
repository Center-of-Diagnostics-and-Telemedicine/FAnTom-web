package components.cutscontainer

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.singleFromFunction
import com.badoo.reaktive.single.subscribeOn
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.*
import repository.ResearchRepository
import store.gridcontainer.MyCutsContainerStore
import store.gridcontainer.MyCutsContainerStore.Intent
import store.gridcontainer.MyCutsContainerStore.State

internal class CutsContainerStoreProvider(
  private val storeFactory: StoreFactory,
  private val researchRepository: ResearchRepository,
  private val researchId: Int,
  private val data: ResearchData,
) {

  fun provide(): MyCutsContainerStore =
    object : MyCutsContainerStore, Store<Intent, State, Nothing> by storeFactory.create(
      name = "MyCutsContainerStore_$researchId",
      initialState = State(
        cuts = listOf(),
        grid = initialFourGrid(ResearchType.CT, false, mapOf())
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
    data class Loaded(val items: List<Cut>, val grid: GridModel) : Result()
  }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Nothing>() {
    override fun executeAction(action: Unit, getState: () -> State) {
      singleFromFunction {
        val grid = when (data.type) {
          ResearchType.CT,
          ResearchType.MG -> initialFourGrid(data.type, data.doseReport, data.modalities)
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
        is Intent.ChangeGrid -> handleGridChanged(intent)
      }.let {}
    }

    private fun handleChangeCutType(getState: () -> State, oldCut: Cut, newCutType: CutType) {
//      val items = getState().grid.updateCuts(data = data, oldCut = oldCut, newCutType = newCutType)
//      dispatch(Result.Loaded(items = items, grid = getState().grid))
    }

    private fun handleGridChanged(intent: Intent.ChangeGrid) {
//      dispatch(Result.Loaded(items = intent.grid.buildCuts(data), grid = intent.grid))
    }
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.Loaded -> copy(cuts = result.items, grid = result.grid)
      }
  }
}