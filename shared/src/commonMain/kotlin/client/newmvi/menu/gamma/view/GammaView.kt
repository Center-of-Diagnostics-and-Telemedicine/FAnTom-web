package client.newmvi.menu.gamma.view

import com.badoo.reaktive.subject.publish.PublishSubject
import model.INITIAL_GAMMA
import client.newmvi.researchmvi.BaseEvent
import client.newmvi.researchmvi.BaseView

interface GammaView : BaseView<GammaView.Event> {

  val events: PublishSubject<Event>

  fun show(model: GammaViewModel)

  data class GammaViewModel(
    val isLoading: Boolean,
    val error: String,
    val value: Double = INITIAL_GAMMA
  )

  sealed class Event : BaseEvent {
    class ChangeValue(val value: Double): Event()
  }
}