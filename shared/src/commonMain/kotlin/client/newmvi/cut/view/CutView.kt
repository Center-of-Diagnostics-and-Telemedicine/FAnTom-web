package client.newmvi.cut.view

import com.badoo.reaktive.subject.publish.PublishSubject
import client.newmvi.researchmvi.BaseEvent
import client.newmvi.researchmvi.BaseView

interface CutView : BaseView<CutView.Event> {

  val events: PublishSubject<Event>

  fun show(model: CutViewModel)

  data class CutViewModel(
    val isLoading: Boolean,
    val error: String,
    val url: String = ""
  )

  sealed class Event : BaseEvent {
    object ErrorShown : Event()
  }
}