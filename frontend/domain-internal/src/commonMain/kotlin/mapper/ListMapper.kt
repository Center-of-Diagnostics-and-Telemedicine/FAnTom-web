package mapper

import controller.ListController.Output
import store.list.FilterStore.Label
import store.list.ListStore.Intent
import store.list.ListStore.State
import view.ListView.Event
import view.ListView.Model

val listStateToListModel: State.() -> Model? = {
  Model(
    items = list,
    error = error,
    loading = loading
  )
}

val listEventToListIntent: Event.() -> Intent? = {
  when (this) {
    Event.DismissError -> Intent.DismissError
    Event.Reload -> Intent.ReloadRequested
    is Event.ItemClick -> null
  }
}

val listEventToOutput: Event.() -> Output? = {
  when (this) {
    is Event.ItemClick -> Output.ItemSelected(id)
    is Event.Reload,
    is Event.DismissError,
    -> null
  }
}

val filterLabelToListIntent: Label.() -> Intent? =
  {
    when (this) {
      is Label.FilterChanged -> Intent.HandleFilterChanged(filter, category)
    }
  }
