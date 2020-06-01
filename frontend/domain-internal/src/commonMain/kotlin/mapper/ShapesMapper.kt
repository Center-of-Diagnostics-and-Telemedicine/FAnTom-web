package mapper

import controller.ShapesController.Input
import controller.ShapesController.Output
import store.cut.ShapesStore.Intent
import store.cut.ShapesStore.State
import view.ShapesView.Event
import view.ShapesView.Model

val inputToShapesIntent: Input.() -> Intent = {
  when (this) {
    is Input.SliceNumberChanged -> Intent.HandleSliceNumberChange(sliceNumber)
    is Input.ExternalSliceNumberChanged -> Intent.HandleExternalSliceNumberChanged(sliceNumber, cut)
  }
}

val shapesEventToShapesIntent: Event.() -> Intent = {
  when (this) {
    is Event.CutTypeOnChange -> TODO()
  }
}

val sliderStateToShapesModel: State.() -> Model = {
  Model(
    verticalCoefficient = verticalCoefficient,
    horizontalCoefficient = horizontalCoefficient,
    sliceNumber = sliceNumber,
    huValue = null
  )
}

val shapesEventToOutput: Event.() -> Output = {
  when (this) {
    is Event.CutTypeOnChange -> TODO()
  }
}
