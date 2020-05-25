package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.Tool
import view.ToolsView.Event
import view.ToolsView.Model

interface ToolsView : MviView<Model, Event> {

  data class Model(
    val items: List<Tool>,
    val current: Tool?
  )

  sealed class Event {
    data class ItemClick(val tool: Tool) : Event()
    object CloseClick : Event()
  }
}

fun initialToolsModel(): Model = Model(
  items = listOf(),
  current = null
)
