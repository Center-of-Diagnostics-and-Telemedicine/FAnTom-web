package store.draw

import com.arkivanov.mvikotlin.core.store.Store
import model.Circle
import store.draw.DrawStore.*

interface DrawStore : Store<Intent, State, Label> {

  data class State(
    val startDicomX: Double,
    val startDicomY: Double,
    val dicomRadius: Double,
    val isDrawing: Boolean = false,
    val isMoving: Boolean = false,
    val isContrastBrightness: Boolean = false,
  ) {
    fun circle(): Circle {
      return Circle(startDicomX, startDicomY, dicomRadius, -1, highlight = false)
    }
  }

  sealed class Intent {
    data class StartDraw(val startDicomX: Double, val startDicomY: Double) : Intent()
    data class StartContrastBrightness(val startDicomX: Double, val startDicomY: Double) : Intent()
    data class StartMouseMove(val startDicomX: Double, val startDicomY: Double) : Intent()

    data class Move(val dicomX: Double, val dicomY: Double) : Intent()
    data class MouseUp(val dicomX: Double, val dicomY: Double) : Intent()
    data class MouseClick(val dicomX: Double, val dicomY: Double, val altKey: Boolean) : Intent()
    data class MouseWheel(val deltaDicomY: Int) : Intent()
    object MouseOut : Intent()
  }

  sealed class Label {

    data class StartMove(val startDicomX: Double, val startDicomY: Double) : Label()
    data class MouseMove(val dicomX: Double, val dicomY: Double) : Label()
    data class MoveInClick(val deltaX: Double, val deltaY: Double) : Label()
    object StopMove : Label()

    data class Drawn(val circle: Circle) : Label()
    data class OnClick(val dicomX: Double, val dicomY: Double, val altKey: Boolean) : Label()
    data class ChangeSlice(val deltaDicomY: Int) : Label()

    data class ChangeContrastBrightness(val deltaX: Double, val deltaY: Double) : Label()

    object ContrastBrightnessChanged : Label()
  }
}
