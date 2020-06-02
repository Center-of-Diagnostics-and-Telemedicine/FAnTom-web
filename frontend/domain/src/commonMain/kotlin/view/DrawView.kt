package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.Circle
import view.DrawView.Event
import view.DrawView.Model

interface DrawView : MviView<Model, Event> {

  data class Model(val circle: Circle?)

  sealed class Event {
    data class MouseDown(
      val x: Double,
      val y: Double,
      val metaKey: Boolean,
      val button: Short,
      val shiftKey: Boolean
    ) : Event()

    data class MouseMove(val x: Double, val y: Double) : Event()

    data class MouseUp(val x: Double, val y: Double) : Event()

    object MouseOut : Event()

    data class MouseClick(val x: Double, val y: Double, val altKey: Boolean, val metaKey: Boolean) :
      Event()

    data class MouseWheel(val deltaY: Double) : Event()
  }
}

fun initialDrawModel(): Model = Model(
  circle = null
)
