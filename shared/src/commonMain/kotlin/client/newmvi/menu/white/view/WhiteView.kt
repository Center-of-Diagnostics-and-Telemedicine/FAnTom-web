package client.newmvi.menu.white.view

import com.badoo.reaktive.subject.publish.PublishSubject
import client.newmvi.researchmvi.BaseEvent
import client.newmvi.researchmvi.BaseView

interface WhiteView : BaseView<WhiteView.Event> {

  val events: PublishSubject<Event>

  fun show(model: WhiteViewModel)

  data class WhiteViewModel(
    val isLoading: Boolean,
    val error: String,
    val value: Double
  )

  sealed class Event : BaseEvent {
    class ChangeValue(val value: Double) : Event()
  }
}