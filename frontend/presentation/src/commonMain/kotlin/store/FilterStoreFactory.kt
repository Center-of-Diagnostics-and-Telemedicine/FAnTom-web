package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.Category
import model.Filter
import store.list.FilterStore.*
import store.list.FilterStoreAbstractFactory

internal class FilterStoreFactory(
  storeFactory: StoreFactory
) : FilterStoreAbstractFactory(
  storeFactory = storeFactory
) {

  override fun createExecutor(): Executor<Intent, Nothing, State, Result, Label> =
    object : ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

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
}
