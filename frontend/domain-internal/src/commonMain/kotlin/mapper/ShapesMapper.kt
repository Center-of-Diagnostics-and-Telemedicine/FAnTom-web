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
    huValue = hounsfield,
    position = position,
    circles = circles,
    rects = rects
  )
}

//val shapesEventToOutput: Event.() -> Output = {
//  when (this) {
//    is Event.CutTypeOnChange -> TODO()
//  }
//}

val drawLabelToShapesIntent: DrawStore.Label.() -> Intent? = {
  when (this) {
    is DrawStore.Label.CenterMarkClick -> Intent.HandleCenterMarkClick(dicomX, dicomY)
    is DrawStore.Label.MoveInClick -> Intent.HandleMoveInClick(deltaX, deltaY)
    else -> null
  }
}

val drawDebounceLabelToShapesIntent: DrawStore.Label.() -> Intent? = {
  when (this) {
    is DrawStore.Label.MouseMove -> Intent.HandleMousePosition(dicomX, dicomY)
    else -> null
  }
}

val cutLabelToShapesIntent: CutStore.Label.() -> Intent? = {
  when (this) {
    is CutStore.Label.SliceNumberChanged -> Intent.HandleSliceNumberChange(sliceNumber)
    is CutStore.Label.ExternalSliceNumberChanged -> {
      Intent.HandleExternalSliceNumberChanged(sliceNumber, externalCut)
    }
    is CutStore.Label.Marks -> Intent.HandleMarks(list)
    is CutStore.Label.StopMoving -> Intent.HandleStopMoving
    is CutStore.Label.StartClick -> Intent.HandleStartClick(startDicomX, startDicomY)
    is CutStore.Label.CircleDrawn -> null
    is CutStore.Label.SelectMark -> null
    is CutStore.Label.CenterMark -> null
    is CutStore.Label.UnselectMark -> null
    is CutStore.Label.ContrastBrightnessChanged -> null
    is CutStore.Label.MarkUpdate -> null
    is CutStore.Label.UpdateMarkWithSave -> null
  }
}
