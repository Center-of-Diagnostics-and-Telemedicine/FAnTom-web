package components.grid

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.*
import repository.GridRepository
import store.tools.MyGridStore
import store.tools.MyGridStore.*

internal class GridStoreProvider(
  private val storeFactory: StoreFactory,
  private val gridRepository: GridRepository,
  private val data: ResearchDataModel,
  private val researchId: Int,
) {

  fun provide(): MyGridStore {
    return object : MyGridStore, Store<Intent, State, Label> by storeFactory.create(
      name = "GridStore_${researchId}",
      initialState = State(
        grid = data.buildDefaultGrid(),
        series = data.series.keys.toList(),
        currentSeries = data.defaultSeries().name
      ),
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }
  }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeAction(action: Unit, getState: () -> State) {
      val state = getState()
      gridRepository
        .grid
        .map(Result::GridChanged)
        .subscribeScoped(onNext = ::dispatch)
      gridRepository.changeGrid(state.grid)
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.ChangeGrid -> handleChangeGrid(intent.gridType, getState())
        is Intent.ChangeSeries -> handleChangeSeries(intent.series, getState())
      }.let {}
    }

    private fun handleChangeGrid(gridType: MyNewGridType, state: State) {
      val grid = data.buildGrid(gridType, state.currentSeries)
      gridRepository.changeGrid(grid)
    }

    private fun handleChangeSeries(series: String, state: State) {
      dispatch(Result.CurrentSeriesChanged(series))
      val grid = data.buildGrid(series)
      gridRepository.changeGrid(grid)
    }
  }

  private sealed class Result : JvmSerializable {
    data class GridChanged(val grid: MyNewGrid) : Result()
    data class CurrentSeriesChanged(val series: String) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.GridChanged -> copy(grid = result.grid)
        is Result.CurrentSeriesChanged -> copy(currentSeries = result.series)
      }

  }
}