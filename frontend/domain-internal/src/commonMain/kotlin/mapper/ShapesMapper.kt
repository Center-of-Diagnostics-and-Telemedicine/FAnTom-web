package mapper

import controller.ShapesController.Input
import controller.ShapesController.Output
import store.cut.ShapesStore.Intent
import store.cut.ShapesStore.State
import view.ShapesView.Event
import view.ShapesView.Model

val inputToShapesIntent: Input.() -> Intent = {
  when (this) {
    is Input.HorizontalLineChange -> Intent.HandleHorizontalLineChanged(line)
    is Input.VerticalLineChange -> Intent.HandleVerticalLineChanged(line)
    is Input.SliceNumberChanged -> Intent.HandleSliceNumberChange(sliceNumber)
  }
}

val shapesEventToShapesIntent: Event.() -> Intent = {
  when (this) {
    is Event.HandleOnChange -> TODO()
  }
}

val sliderStateToShapesModel: State.() -> Model = {
  Model(
    vertical = verticalLine,
    horizontal = horizontalLine,
    sliceNumber = sliceNumber,
    huValue = null
  )
}

val shapesEventToOutput: Event.() -> Output = {
  when (this) {
    is Event.HandleOnChange -> TODO()
  }
}
