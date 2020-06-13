package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.Tool
import view.MarksView.Event
import view.MarksView.Model

interface MarksView : MviView<Model, Event> {

  data class Model(
    val items: List<Tool>,
    val current: Tool?
  )

  sealed class Event {
    data class ItemClick(val tool: Tool) : Event()
    object CloseClick : Event()
  }
}

fun initialMarksModel(): Model = Model(
  items = listOf(),
  current = null
)
