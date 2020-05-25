package mapper

import store.GridStore.Intent
import store.GridStore.State
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
      is Event.ItemClick -> Intent.HandleGridClick(grid)
    }
  }
