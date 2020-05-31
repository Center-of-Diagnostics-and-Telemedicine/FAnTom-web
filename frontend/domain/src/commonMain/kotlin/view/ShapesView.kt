package view

import com.arkivanov.mvikotlin.core.view.MviView
import view.ShapesView.Event
import view.ShapesView.Model

interface ShapesView : MviView<Model, Event> {

  data class Model(
    val vertical: Int,
    val horizontal: Int,
    val sliceNumber: Int,
    val huValue: Int?
  )

  sealed class Event {
    data class HandleOnChange(val value: Int) : Event()
  }
}

fun initialShapesModel(): Model = Model(
  vertical = 0,
  horizontal = 0,
  sliceNumber = 1,
  huValue = null
)
