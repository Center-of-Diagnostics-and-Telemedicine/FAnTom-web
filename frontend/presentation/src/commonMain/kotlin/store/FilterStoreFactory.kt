package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.Filter
import store.FilterStore.*

internal class FilterStoreFactory(
  storeFactory: StoreFactory
) : FilterStoreAbstractFactory(
  storeFactory = storeFactory
) {

  override fun createExecutor(): Executor<Intent, Nothing, State, Result, Label> =
    object : ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

      override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
          is Intent.HandleFilterClick -> changeFilter(intent.filter)
        }.let {}
      }

      private fun changeFilter(filter: Filter) {
        dispatch(Result.FilterChanged(filter))
        publish(Label.FilterChanged(filter))
      }
    }
}
