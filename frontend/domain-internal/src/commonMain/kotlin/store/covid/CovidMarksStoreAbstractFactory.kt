package store.covid

import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.LungLobeModel
import model.initialLungLobeModels
import store.covid.CovidMarksStore.*

abstract class CovidMarksStoreAbstractFactory(
  private val storeFactory: StoreFactory
) {

  fun create(): CovidMarksStore =
    object : CovidMarksStore, Store<Intent, State, Label> by storeFactory.create(
      name = "CovidMarksStore",
      initialState = State(initialLungLobeModels),
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  protected abstract fun createExecutor(): Executor<Intent, Unit, State, Result, Label>

  protected sealed class Result : JvmSerializable {
    object Loading : Result()
    data class Loaded(val covidLungLobes: Map<Int, LungLobeModel>) : Result()
    data class Error(val error: String) : Result()
    object DismissErrorRequested : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.Loading -> copy(loading = true)
        is Result.Loaded -> copy(covidLungLobes = result.covidLungLobes)
        is Result.DismissErrorRequested -> copy(error = "")
        is Result.Error -> copy(error = result.error, loading = false)
      }
  }
}