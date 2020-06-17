package store.marks

import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.MarkDomain
import store.marks.MarksStore.Intent
import store.marks.MarksStore.State
import store.marks.MarksStore.Label

abstract class MarksStoreAbstractFactory(
  private val storeFactory: StoreFactory
) {

  fun create(): MarksStore =
    object : MarksStore, Store<Intent, State, Label> by storeFactory.create(
      name = "MarksStore",
      initialState = State(),
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
    data class Loaded(val marks: List<MarkDomain>) : Result()
    data class Error(val error: String) : Result()

    object DismissErrorRequested : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.Loading -> copy(loading = true)
        is Result.Loaded -> copy(marks = result.marks, loading = false)
        is Result.Error -> copy(error = result.error, loading = false)
        is Result.DismissErrorRequested -> copy(error = "")
      }
  }
}
