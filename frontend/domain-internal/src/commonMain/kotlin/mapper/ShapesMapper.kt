package mapper

import controller.ShapesController.Input
import controller.ShapesController.Output
import store.shapes.ShapesStore.Intent
import store.shapes.ShapesStore.State
import view.ShapesView.Event
import view.ShapesView.Model

val inputToShapesIntent: Input.() -> Intent = {
  when (this) {
    is Input.SliceNumberChanged -> Intent.HandleSliceNumberChange(sliceNumber)
    is Input.ExternalSliceNumberChanged -> Intent.HandleExternalSliceNumberChanged(sliceNumber, cut)
    is Input.MousePosition ->
      Intent.HandleMousePosition(dicomX = dicomX, dicomY = dicomY, cutType = cutType)
    is Input.Drawing ->
      Intent.HandleDrawing(circle = circle, cutType = cutType)
  }
}

val shapesEventToShapesIntent: Event.() -> Intent = {
  when (this) {
    is Event.CutTypeOnChange -> TODO()
  }
}

val shapesStateToShapesModel: State.() -> Model = {
  Model(
    verticalCoefficient = verticalCoefficient,
    horizontalCoefficient = horizontalCoefficient,
    sliceNumber = sliceNumber,
    huValue = null,
    position = position
  )
}

val shapesEventToOutput: Event.() -> Output = {
  when (this) {
    is Event.CutTypeOnChange -> TODO()
  }
}
