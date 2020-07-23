package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.MarkModel
import view.MarksView.Event
import view.MarksView.Model

interface MarksView : MviView<Model, Event> {

  data class Model(
    val items: List<MarkModel>,
    val current: MarkModel?
  )

  sealed class Event {
    data class SelectItem(val mark: MarkModel) : Event()
    data class ItemCommentChanged(val mark: MarkModel, val comment: String) : Event()
    data class DeleteItem(val mark: MarkModel) : Event()
  }
}

fun initialMarksModel(): Model = Model(
  items = listOf(),
  current = null
)
