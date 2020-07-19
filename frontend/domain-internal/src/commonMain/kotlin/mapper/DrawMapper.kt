package mapper

import model.Circle
import model.LEFT_MOUSE_BUTTON
import model.MIDDLE_MOUSE_BUTTON
import store.draw.DrawStore.Intent
import store.draw.DrawStore.State
import view.DrawView.Event
import view.DrawView.Model

val drawEventToDrawIntent: Event.() -> Intent = {
  when (this) {
    is Event.MouseDown -> mapMouseDown(this)
    is Event.MouseMove -> Intent.Move(dicomX = x, dicomY = y)
    is Event.MouseUp -> Intent.MouseUp(dicomY = y, dicomX = x)
    Event.MouseOut -> Intent.MouseOut
    is Event.MouseWheel -> Intent.MouseWheel(deltaDicomY = deltaY)
  }
}

fun mapMouseDown(event: Event.MouseDown): Intent {
  val isDraw = event.metaKey && event.button == LEFT_MOUSE_BUTTON
  val isContrastBrightness = event.button == MIDDLE_MOUSE_BUTTON
  val isCenterMark = event.altKey
  return when {
    isDraw -> Intent.StartDraw(startDicomX = event.x, startDicomY = event.y)
    isContrastBrightness ->
      Intent.StartContrastBrightness(startDicomX = event.x, startDicomY = event.y)
    isCenterMark -> Intent.CenterMarkClick(startDicomX = event.x, startDicomY = event.y)
    else -> Intent.StartMouseClick(startDicomY = event.y, startDicomX = event.x)
  }
}

val drawStateToDrawModel: State.() -> Model = {
  if (dicomRadiusHorizontal != 0.0) {
    Model(
      circle = Circle(
        dicomCenterX = startDicomX,
        dicomCenterY = startDicomY,
        dicomRadiusHorizontal = dicomRadiusHorizontal,
        dicomRadiusVertical = dicomRadiusVertical,
        id = -1,
        highlight = false,
        isCenter = false
      )
    )
  } else {
    Model(null)
  }

}

//val drawLabelToOutput: Label.() -> Output = {
//  when (this) {
//    is Label.StartMove -> Output.StartMoving(
//      startDicomX = startDicomX,
//      startDicomY = startDicomY
//    )
//    is Label.ChangeContrastBrightness -> Output.ChangeContrastBrightness(
//      deltaX = deltaX,
//      deltaY = deltaY
//    )
//    is Label.MouseMove -> Output.MousePosition(dicomX = dicomX, dicomY = dicomY, cutType = cutType)
//    is Label.Drawn -> Output.Drawn(circle = circle, cutType = cutType)
//    is Label.OnClick -> Output.OnClick(
//      dicomX = dicomX,
//      dicomY = dicomY,
//      altKeyPressed = altKey,
//      cutType = type,
//    )
//    is Label.ChangeSlice -> Output.ChangeSlice(deltaY = deltaDicomY, type = cutType)
//  }
//}


