package mapper

import store.list.FilterStore.Intent
import store.list.FilterStore.State
import view.FilterView.Event
import view.FilterView.Model

val filterStateToFilterModel: State.() -> Model? = { Model(items = list, current = current) }

val filterEventToFilterIntent: Event.() -> Intent? =
  {
    when (this) {
      is Event.ItemClick -> Intent.HandleFilterClick(filter)
    }
  }
