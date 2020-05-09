package client.newmvi.cut.view

import client.newmvi.researchmvi.BaseEvent
import client.newmvi.researchmvi.BaseView
import com.badoo.reaktive.subject.publish.PublishSubject

interface CutView : BaseView<CutView.Event> {

  val events: PublishSubject<Event>

  fun show(model: CutViewModel)

  data class CutViewModel(
    val isLoading: Boolean,
    val error: String,
    val img: String
  )

  sealed class Event : BaseEvent {
    object ErrorShown : Event()
  }
}