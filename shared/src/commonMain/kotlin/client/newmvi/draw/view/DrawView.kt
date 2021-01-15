package client.newmvi.draw.view

import com.badoo.reaktive.subject.publish.PublishSubject
import client.newmvi.researchmvi.BaseEvent
import client.newmvi.researchmvi.BaseView
import model.Circle

interface DrawView : BaseView<DrawView.Event> {

  val events: PublishSubject<Event>

  fun show(model: DrawViewModel)

  data class DrawViewModel(
    val circle: Circle? = null
  )

  sealed class Event : BaseEvent {
    class MouseDown(
      val x: Double,
      val y: Double,
      val metaKey: Boolean,
      val button: Short,
      val shiftKey: Boolean
    ) : Event()

    class MouseMove(
      val x: Double,
      val y: Double
    ) : Event()

    class MouseUp(
      val x: Double,
      val y: Double
    ) : Event()

    object MouseOut : Event()

    class MouseClick(
      val x: Double,
      val y: Double,
      val altKey: Boolean,
      val metaKey: Boolean
    ) : Event()

    class MouseWheel(val deltaY: Double) : Event()
  }
}