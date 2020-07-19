package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.MarkDomain
import view.MarksView.Event
import view.MarksView.Model

interface MarksView : MviView<Model, Event> {

  data class Model(
    val items: List<MarkDomain>,
    val current: MarkDomain?
  )

  sealed class Event {
    data class SelectItem(val mark: MarkDomain) : Event()
    data class ItemCommentChanged(val mark: MarkDomain, val comment: String) : Event()
    data class DeleteItem(val mark: MarkDomain) : Event()
  }
}

fun initialMarksModel(): Model = Model(
  items = listOf(),
  current = null
)
