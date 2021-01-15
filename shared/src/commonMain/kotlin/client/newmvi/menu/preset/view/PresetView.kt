package client.newmvi.menu.preset.view

import com.badoo.reaktive.subject.publish.PublishSubject
import client.newmvi.researchmvi.BaseEvent
import client.newmvi.researchmvi.BaseView

interface PresetView : BaseView<PresetView.Event> {

  val events: PublishSubject<Event>

  fun show(model: PresetViewModel)

  data class PresetViewModel(
    val isLoading: Boolean,
    val error: String,
    val value: String
  )

  sealed class Event : BaseEvent {
    class ChangeValue(val value: String) : Event()
  }
}