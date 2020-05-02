package client.newmvi.draw.binder

import client.newmvi.draw.store.DrawStore
import client.newmvi.draw.view.DrawView
import model.*

internal object DrawViewEventToIntentMapper {

  operator fun invoke(event: DrawView.Event): DrawStore.Intent =
    when (event) {
      is DrawView.Event.MouseDown -> {
        val isDraw = event.metaKey && event.button == LEFT_MOUSE_BUTTON
        val isContrastBrightness = event.button == MIDDLE_MOUSE_BUTTON
        when {
          isDraw -> DrawStore.Intent.StartDraw(startX = event.x, startY = event.y)
          isContrastBrightness ->
            DrawStore.Intent.StartContrastBrightness(startX = event.x, startY = event.y)
          else -> DrawStore.Intent.StartMouseMove(
            startY = event.y,
            startX = event.x,
            shiftKey = event.shiftKey
          )
        }
      }
      is DrawView.Event.MouseMove -> DrawStore.Intent.Move(x = event.x, y = event.y)
      is DrawView.Event.MouseUp -> DrawStore.Intent.MouseUp(x = event.x, y = event.y)
      is DrawView.Event.MouseOut -> DrawStore.Intent.MouseOut
      is DrawView.Event.MouseClick -> DrawStore.Intent.MouseClick(
        event.x,
        event.y,
        event.altKey
      )
      is DrawView.Event.MouseWheel -> DrawStore.Intent.ChangeSliceNumber(
        deltaY = if (event.deltaY < 0.0) -1 else 1
      )
    }
}