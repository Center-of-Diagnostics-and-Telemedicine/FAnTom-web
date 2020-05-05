package client.newmvi.menu.black.view

import client.newmvi.researchmvi.BaseEvent
import client.newmvi.researchmvi.BaseView
import com.badoo.reaktive.subject.publish.PublishSubject
import model.INITIAL_BLACK

interface BlackView : BaseView<BlackView.Event> {

  val events: PublishSubject<Event>

  fun show(model: BlackViewModel)

  data class BlackViewModel(
    val isLoading: Boolean,
    val error: String,
    val value: Double = INITIAL_BLACK
  )

  sealed class Event : BaseEvent {
    class ChangeValue(val value: Double) : Event()
  }
}