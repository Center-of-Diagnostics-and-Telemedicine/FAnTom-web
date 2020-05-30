package mapper

import store.tools.GridStore.Intent
import store.tools.GridStore.State
import  view.GridView.Event
import  view.GridView.Model

val gridStateToGridModel: State.() -> Model? = {
  Model(
    items = list,
    current = current
  )
}

val gridEventToGridIntent: Event.() -> Intent? =
  {
    when (this) {
      is Event.ItemClick -> Intent.HandleGridClick(gridType)
    }
  }
