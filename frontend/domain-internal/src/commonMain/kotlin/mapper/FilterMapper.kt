package mapper

import store.FilterStore.Intent
import store.FilterStore.State
import view.FilterView.Event
import view.FilterView.Model

val filterStateToFilterModel: State.() -> Model? = { Model(items = listOf()) }

val addEventToAddIntent: Event.() -> Intent? =
  {
    when (this) {
      is Event.ItemClick -> Intent.HandleFilterClick(filter)
    }
  }
