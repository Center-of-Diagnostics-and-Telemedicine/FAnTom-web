package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.MarkModel
import model.MarkTypeModel
import view.MarksView.Event
import view.MarksView.Model

interface MarksView : MviView<Model, Event> {

  data class Model(
    val items: List<MarkModel>,
    val markTypes: List<MarkTypeModel>,
    val error: String
  )

  sealed class Event {
    data class SelectItem(val mark: MarkModel) : Event()
    data class ItemCommentChanged(val mark: MarkModel, val comment: String) : Event()
    data class DeleteItem(val mark: MarkModel) : Event()
    data class ChangeMarkType(val type: MarkTypeModel, val markId: Int) : Event()
    data class ChangeVisibility(val mark: MarkModel) : Event()

    object DissmissError : Event()
  }
}

fun initialMarksModel(): Model = Model(
  items = listOf(),
  markTypes = listOf(),
  error = ""
)
