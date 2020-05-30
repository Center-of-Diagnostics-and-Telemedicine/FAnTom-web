package mapper

import controller.ToolsController.Output
import store.tools.ToolsStore.Intent
import store.tools.ToolsStore.State
import view.ToolsView.Event
import view.ToolsView.Model

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

val toolsEventToToolsOutput: Event.() -> Output? = {
  when (this) {
    is Event.ItemClick -> null
    is Event.CloseClick -> Output.Close
  }
}
