package mapper

import model.*
import store.draw.DrawStore.Intent
import store.draw.DrawStore.State
import store.userinput.UserInputStore
import view.DrawView.Event
import view.DrawView.Model

val drawEventToDrawIntent: Event.() -> Intent? = {
  when (this) {
    is Event.MouseDown -> mapMouseDown(this)
    is Event.MouseMove -> Intent.Move(dicomX = x, dicomY = y)
    is Event.MouseUp -> Intent.MouseUp
    Event.MouseOut -> Intent.MouseOut
    is Event.MouseWheel -> Intent.MouseWheel(deltaDicomY = deltaY)
    Event.DoubleClick -> Intent.DoubleClick
  }
}

val drawEventToUserInputIntent: Event.() -> UserInputStore.Intent? = {
  when (this) {
    is Event.MouseDown -> {
      val isContrastBrightness = button == MIDDLE_MOUSE_BUTTON
      if (isContrastBrightness)
        UserInputStore.Intent.StartContrastBrightness(startDicomX = x, startDicomY = y)
      else null
    }
    is Event.MouseMove -> UserInputStore.Intent.MouseMove(dicomX = x, dicomY = y)
    Event.MouseOut -> UserInputStore.Intent.MouseOut
    Event.DoubleClick -> UserInputStore.Intent.DoubleClick
    is Event.MouseWheel -> UserInputStore.Intent.MouseWheel(deltaDicomY = deltaY)
    is Event.MouseUp -> UserInputStore.Intent.MouseUp
  }
}

fun mapMouseDown(event: Event.MouseDown): Intent {
  val isDrawEllipse = event.metaKey && event.button == LEFT_MOUSE_BUTTON
  val isDrawRectangle = event.shiftKey && event.button == LEFT_MOUSE_BUTTON
  val isContrastBrightness = event.button == MIDDLE_MOUSE_BUTTON
  return when {
    isDrawEllipse -> Intent.StartDrawEllipse(startDicomX = event.x, startDicomY = event.y)
    isDrawRectangle -> Intent.StartDrawRectangle(startDicomX = event.x, startDicomY = event.y)
    isContrastBrightness ->
      Intent.StartContrastBrightness(startDicomX = event.x, startDicomY = event.y)
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
        isCenter = false,
        color = defaultMarkColor
      )
      isDrawingEllipse -> Circle(
        dicomCenterX = startDicomX,
        dicomCenterY = startDicomY,
        dicomRadiusHorizontal = dicomRadiusHorizontal,
        dicomRadiusVertical = dicomRadiusVertical,
        id = -1,
        highlight = false,
        isCenter = false,
        color = defaultMarkColor
      )
      else -> null
    }
    Model(shape)
  } else {
    emptyModel
  }
}

val userInputStateToDrawModel: UserInputStore.State.() -> Model = {
  Model(null)
}


