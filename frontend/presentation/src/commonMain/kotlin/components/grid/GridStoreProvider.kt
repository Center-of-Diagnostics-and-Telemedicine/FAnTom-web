package components.grid

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
import repository.GridRepository
import store.tools.MyGridStore
import store.tools.MyGridStore.*

internal class GridStoreProvider(
  private val storeFactory: StoreFactory,
  private val gridRepository: GridRepository,
  private val data: ResearchData,
  private val researchId: Int,
) {

  val initialState = State(
    grid = GridType.initial,
  )

  fun provide(): MyGridStore {
    return object : MyGridStore, Store<Intent, State, Label> by storeFactory.create(
      name = "MyGridStore_${researchId}",
      initialState = initialState,
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }
  }

  private sealed class Result : JvmSerializable {
    data class GridChanged(val grid: GridType) : Result()
  }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeAction(action: Unit, getState: () -> State) {
      gridRepository
        .grid
        .map(Result::GridChanged)
        .subscribeScoped(onNext = ::dispatch)
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.ChangeGrid -> gridRepository.changeGrid(intent.gridType)
      }.let {}
    }
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.GridChanged -> copy(grid = result.grid)
      }

  }
}