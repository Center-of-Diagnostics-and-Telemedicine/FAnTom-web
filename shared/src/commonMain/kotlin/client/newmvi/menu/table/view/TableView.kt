package client.newmvi.menu.table.view

import com.badoo.reaktive.subject.publish.PublishSubject
import client.newmvi.researchmvi.BaseEvent
import client.newmvi.researchmvi.BaseView
import model.Mark

interface TableView : BaseView<TableView.Event> {

  val events: PublishSubject<Event>

  fun show(model: TableViewModel)

  data class TableViewModel(
    val isLoading: Boolean,
    val error: String,
    val areas: List<Mark> = listOf(),
    val selectedAreaId: Int
  )

  sealed class Event : BaseEvent {
    class Delete(val value: Mark) : Event()
    class Select(val area: Mark): Event()
    class ChangeMarkType(val id: Int,val type: Int) : Event()
    class ChangeComment(val id: Int, val comment: String) : Event()
  }
}
