package components.series

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.ResearchDataModel
import model.SeriesModel
import model.initialSeries
import model.series
import repository.SeriesRepository
import store.tools.SeriesStore
import store.tools.SeriesStore.*

internal class SeriesStoreProvider(
  private val storeFactory: StoreFactory,
  private val researchId: Int,
  private val seriesRepository: SeriesRepository,
  dataModel: ResearchDataModel
) {

  val initialState = State(
    series = dataModel.series(),
    currentSeries = dataModel.initialSeries()
  )

  fun provide(): SeriesStore {
    return object : SeriesStore, Store<Intent, State, Label> by storeFactory.create(
      name = "SeriesStore_${researchId}",
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

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeAction(action: Unit, getState: () -> State) {
      seriesRepository.seriesLoaded(getState().series)
      seriesRepository.changeSeries(getState().currentSeries)

      seriesRepository
        .series
        .map(Result::Loaded)
        .subscribeScoped(onNext = ::dispatch)
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.ChangeSeries -> handleSeriesChange(intent.seriesName, getState().series)
      }.let {}
    }

    private fun handleSeriesChange(seriesName: String, series: Map<String, SeriesModel>) {
      series[seriesName]?.let {
        dispatch(Result.CurrentSeriesChanged(it))
        seriesRepository.changeSeries(it)
      }
    }
  }

  private sealed class Result : JvmSerializable {
    data class CurrentSeriesChanged(val current: SeriesModel) : Result()
    data class Loaded(val series: Map<String, SeriesModel>) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.CurrentSeriesChanged -> copy(currentSeries = result.current)
        is Result.Loaded -> copy(series = result.series)
      }

  }
}