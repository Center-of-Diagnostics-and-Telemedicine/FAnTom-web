package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.Shape
import view.DrawView.Event
import view.DrawView.Model

interface DrawView : MviView<Model, Event> {

  data class Model(val shape: Shape?)

  sealed class Event {
    data class MouseDown(
      val x: Double,
      val y: Double,
      val metaKey: Boolean,
      val button: Short,
      val shiftKey: Boolean,
      val altKey: Boolean
    ) : Event()

    data class MouseMove(val x: Double, val y: Double) : Event()

    object MouseUp : Event()

    object MouseOut : Event()
    object DoubleClick : Event()

    data class MouseWheel(val deltaY: Int) : Event()
  }
}

fun initialDrawModel(): Model = Model(shape = null)
