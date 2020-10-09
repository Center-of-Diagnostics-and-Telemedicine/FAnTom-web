package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.CutType
import model.PointPosition
import model.Rect
import model.Shape
import view.ShapesView.Event
import view.ShapesView.Model

interface ShapesView : MviView<Model, Event> {

  data class Model(
    val verticalCoefficient: Double,
    val horizontalCoefficient: Double,
    val sliceNumber: Int,
    val huValue: Int?,
    val position: PointPosition?,
    val shapes: List<Shape>,
    val rects: List<Rect>
  )

  sealed class Event {
    data class CutTypeOnChange(val value: CutType) : Event()
  }
}

fun initialShapesModel(): Model = Model(
  verticalCoefficient = 0.0,
  horizontalCoefficient = 0.0,
  sliceNumber = 1,
  huValue = null,
  position = null,
  shapes = listOf(),
  rects = listOf()
)
