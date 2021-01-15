package client.newmvi.researchlist.view

import com.badoo.reaktive.subject.publish.PublishSubject
import client.newmvi.researchmvi.BaseEvent
import client.newmvi.researchmvi.BaseView
import model.Research

interface ResearchListView : BaseView<ResearchListView.Event> {

  val events: PublishSubject<Event>

  fun show(model: ResearchListViewModel)

  data class ResearchListViewModel(
    val isLoading: Boolean,
    val error: String,
    val data: List<Research>
  )

  sealed class Event : BaseEvent {
    object Init : Event()
    object ErrorShown : Event()

  }
}