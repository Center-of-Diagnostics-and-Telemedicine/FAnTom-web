package client.newmvi.slider.view

import com.badoo.reaktive.subject.publish.PublishSubject
import client.newmvi.researchmvi.BaseEvent
import client.newmvi.researchmvi.BaseView

interface SliderView : BaseView<SliderView.Event> {

  val events: PublishSubject<Event>

  fun show(model: SliderViewModel)

  data class SliderViewModel(
    val isLoading: Boolean,
    val error: String,
    val value: Int = 0
  )

  sealed class Event : BaseEvent {
    class Drag(val value: Int): Event()
  }
}