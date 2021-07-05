package components.cutscontainer

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.GridType
import model.ResearchData
import model.ResearchDataModel
import model.buildModel
import repository.GridRepository
import repository.ResearchRepository
import store.gridcontainer.MyCutsContainerStore
import store.gridcontainer.MyCutsContainerStore.Intent
import store.gridcontainer.MyCutsContainerStore.State

internal class CutsContainerStoreProvider(
  private val storeFactory: StoreFactory,
  private val researchRepository: ResearchRepository,
  private val gridRepository: GridRepository,
  private val researchId: Int,
  private val data: ResearchDataModel,
) {

  fun provide(): MyCutsContainerStore =
    object : MyCutsContainerStore, Store<Intent, State, Nothing> by storeFactory.create(
      name = "CutsContainerStore_$researchId",
      initialState = State(
        gridType = GridType.initial,
        gridModel = GridType.initial.buildModel(data)
      ),
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  private sealed class Result : JvmSerializable {
    data class GridChanged(val grid: GridType) : Result()
  }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Nothing>() {
    override fun executeAction(action: Unit, getState: () -> State) {
      gridRepository
        .grid
        .map(Result::GridChanged)
        .subscribeScoped(onNext = ::dispatch)
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        else -> throw NotImplementedError("not implemented for $intent")
      }.let {}
    }
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.GridChanged -> copy(gridType = result.grid)
      }
  }
}