package mapper

import store.FilterStore.Intent
import store.FilterStore.State
import view.FilterView.Event
import view.FilterView.Model

val filterStateToFilterModel: State.() -> Model? = { Model(items = list, current = current) }

val filterEventToFilterIntent: Event.() -> Intent? =
  {
    when (this) {
      is Event.ItemClick -> Intent.HandleFilterClick(filter)
    }
  }
