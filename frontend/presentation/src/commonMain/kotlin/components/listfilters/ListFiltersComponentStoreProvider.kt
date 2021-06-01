package components.listfilters

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.Category
import model.Filter
import store.list.FilterStore
import store.list.FilterStore.*

internal class ListFiltersComponentStoreProvider(
  private val storeFactory: StoreFactory,
) {

  fun provide(): FilterStore =
    object : FilterStore, Store<Intent, State, Label> by storeFactory.create(
      name = "FilterStore",
      initialState = State(),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  private sealed class Result : JvmSerializable {
    data class FilterChanged(val filter: Filter) : Result()
    data class CategoryChanged(val category: Category) : Result()
  }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleFilterClick -> changeFilter(intent.filter, getState)
        is Intent.HandleCategoryClick -> changeCategory(intent.category, getState)
      }.let {}
    }

    private fun changeFilter(filter: Filter, getState: () -> State) {
      dispatch(Result.FilterChanged(filter))
      publish(Label.FilterChanged(filter, getState().currentCategory))
    }

    private fun changeCategory(category: Category, getState: () -> State) {
      dispatch(Result.CategoryChanged(category))
      publish(Label.FilterChanged(getState().currentFilter, category))
    }
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.FilterChanged -> copy(currentFilter = result.filter)
        is Result.CategoryChanged -> copy(currentCategory = result.category)
      }
  }
}