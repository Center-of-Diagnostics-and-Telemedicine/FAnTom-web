package client.newmvi.menu.mipvalue.view

import com.badoo.reaktive.subject.publish.PublishSubject
import model.INITIAL_MIP_VALUE
import client.newmvi.researchmvi.BaseEvent
import client.newmvi.researchmvi.BaseView

interface MipValueView : BaseView<MipValueView.Event> {

  val events: PublishSubject<Event>

  fun show(model: MipValueViewModel)

  data class MipValueViewModel(
    val isLoading: Boolean,
    val error: String,
    val value: Int = INITIAL_MIP_VALUE,
    val available: Boolean
  )

  sealed class Event : BaseEvent {
    class ChangeMethod(val value: Int) : Event()
  }
}