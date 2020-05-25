package mapper

import store.ToolsStore.Intent
import store.ToolsStore.State
import  view.ToolsView.Event
import  view.ToolsView.Model

val toolsStateToToolsModel: State.() -> Model? = {
  Model(
    items = list,
    current = current
  )
}

val toolsEventToToolsIntent: Event.() -> Intent? =
  {
    when (this) {
      is Event.ItemClick -> Intent.HandleToolClick(tool)
      Event.CloseClick -> throw NoSuchElementException("CloseClick event not implemented")
    }
  }
