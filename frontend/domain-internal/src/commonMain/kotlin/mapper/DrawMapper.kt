package mapper

import model.Circle
import model.LEFT_MOUSE_BUTTON
import model.MIDDLE_MOUSE_BUTTON
import model.Rectangle
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
    Event.DoubleClick -> Intent.DoubleClick
  }
}

fun mapMouseDown(event: Event.MouseDown): Intent {
  val isDrawEllipse = event.metaKey && event.button == LEFT_MOUSE_BUTTON
  val isDrawRectangle = event.shiftKey && event.button == LEFT_MOUSE_BUTTON
  println("isDrawRectangle = $isDrawRectangle")
  val isContrastBrightness = event.button == MIDDLE_MOUSE_BUTTON
  val isCenterMark = event.altKey
  return when {
    isDrawEllipse -> Intent.StartDrawEllipse(startDicomX = event.x, startDicomY = event.y)
    isDrawRectangle -> Intent.StartDrawRectangle(startDicomX = event.x, startDicomY = event.y)
    isContrastBrightness ->
      Intent.StartContrastBrightness(startDicomX = event.x, startDicomY = event.y)
    isCenterMark -> Intent.CenterMarkClick(startDicomX = event.x, startDicomY = event.y)
    else -> Intent.StartMouseClick(startDicomY = event.y, startDicomX = event.x)
  }
}

val drawStateToDrawModel: State.() -> Model = {
  val emptyModel = Model(null)
  if (dicomRadiusHorizontal != 0.0) {
    val shape = when {
      isDrawingRectangle -> Rectangle(
        dicomCenterX = startDicomX,
        dicomCenterY = startDicomY,
        dicomRadiusHorizontal = dicomRadiusHorizontal,
        dicomRadiusVertical = dicomRadiusVertical,
        id = -1,
        highlight = false,
        isCenter = false
      )
      isDrawingEllipse -> Circle(
        dicomCenterX = startDicomX,
        dicomCenterY = startDicomY,
        dicomRadiusHorizontal = dicomRadiusHorizontal,
        dicomRadiusVertical = dicomRadiusVertical,
        id = -1,
        highlight = false,
        isCenter = false
      )
      else -> null
    }
    Model(shape)
  } else {
    emptyModel
  }
}


