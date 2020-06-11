package mapper

import store.cut.CutStore
import store.draw.DrawStore
import store.shapes.ShapesStore.Intent
import store.shapes.ShapesStore.State
import view.ShapesView.Event
import view.ShapesView.Model

//val inputToShapesIntent: Input.() -> Intent = {
//  when (this) {
//    is Input.SliceNumberChanged -> Intent.HandleSliceNumberChange(sliceNumber)
//    is Input.ExternalSliceNumberChanged -> Intent.HandleExternalSliceNumberChanged(sliceNumber, cut)
//    is Input.MousePosition ->
//      Intent.HandleMousePosition(dicomX = dicomX, dicomY = dicomY, cutType = cutType)
//  }
//}

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

//val shapesEventToOutput: Event.() -> Output = {
//  when (this) {
//    is Event.CutTypeOnChange -> TODO()
//  }
//}

val drawLabelToShapesIntent: DrawStore.Label.() -> Intent? = {
  when (this) {
    is DrawStore.Label.MouseMove -> Intent.HandleMousePosition(dicomX, dicomY)
    is DrawStore.Label.StartMove -> null
    is DrawStore.Label.ChangeContrastBrightness -> TODO()
    is DrawStore.Label.Drawn -> TODO()
    is DrawStore.Label.OnClick -> TODO()
    is DrawStore.Label.ChangeSlice -> TODO()
  }
}

val cutLabelToShapesIntent: CutStore.Label.() -> Intent? = {
  when (this) {
    is CutStore.Label.SliceNumberChanged -> Intent.HandleSliceNumberChange(sliceNumber)
    is CutStore.Label.ExternalSliceNumberChanged -> {
      Intent.HandleExternalSliceNumberChanged(sliceNumber, externalCut)
    }
  }
}
