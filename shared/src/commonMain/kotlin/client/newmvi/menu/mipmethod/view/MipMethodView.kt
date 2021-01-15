package client.newmvi.menu.mipmethod.view

import com.badoo.reaktive.subject.publish.PublishSubject
import client.newmvi.researchmvi.BaseEvent
import client.newmvi.researchmvi.BaseView

interface MipMethodView : BaseView<MipMethodView.Event> {

  val events: PublishSubject<Event>

  fun show(model: MipMethodViewModel)

  data class MipMethodViewModel(
    val isLoading: Boolean,
    val error: String,
    val value: String
  )

  sealed class Event : BaseEvent {
    class ChangeMethod(val value: String): Event()
  }
}