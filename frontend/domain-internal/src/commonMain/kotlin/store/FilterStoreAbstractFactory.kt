package store

import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.Filter
import store.FilterStore.*

abstract class FilterStoreAbstractFactory(
  private val storeFactory: StoreFactory
) {

  fun create(): FilterStore =
    object : FilterStore, Store<Intent, State, Label> by storeFactory.create(
      name = "FilterStore",
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
    data class Loaded(val list: List<Filter>, val current: Filter = Filter.All) : Result()
    data class FilterChanged(val filter: Filter) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.Loaded -> copy(list = result.list, current = result.current)
        is Result.FilterChanged -> copy(current = result.filter)
      }
  }
}