package store.draw

import com.arkivanov.mvikotlin.core.store.Store
import model.Circle
import store.draw.DrawStore.*

interface DrawStore : Store<Intent, State, Label> {

  data class State(
    val startDicomX: Double,
    val startDicomY: Double,
    val dicomRadiusHorizontal: Double,
    val dicomRadiusVertical: Double,
    val isDrawing: Boolean = false,
    val isMoving: Boolean = false,
    val isContrastBrightness: Boolean = false,
  ) {
    fun circle(planar: Boolean): Circle {
      return if (planar) {
        Circle(
          dicomCenterX = startDicomX + (dicomRadiusHorizontal / 2),
          dicomCenterY = startDicomY + (dicomRadiusVertical / 2),
          dicomRadiusHorizontal = dicomRadiusHorizontal,
          dicomRadiusVertical = dicomRadiusVertical,
          id = -1,
          highlight = false
        )
      } else {
        Circle(
          dicomCenterX = startDicomX,
          dicomCenterY = startDicomY,
          dicomRadiusHorizontal = dicomRadiusHorizontal,
          dicomRadiusVertical = dicomRadiusVertical,
          id = -1,
          highlight = false
        )
      }
    }
  }

  sealed class Intent {
    data class StartDraw(val startDicomX: Double, val startDicomY: Double) : Intent()
    data class StartContrastBrightness(val startDicomX: Double, val startDicomY: Double) : Intent()
    data class StartMouseClick(val startDicomX: Double, val startDicomY: Double) : Intent()

    data class Move(val dicomX: Double, val dicomY: Double) : Intent()
    data class MouseUp(val dicomX: Double, val dicomY: Double) : Intent()
    data class CenterMarkClick(val startDicomX: Double, val startDicomY: Double) : Intent()
    data class MouseWheel(val deltaDicomY: Int) : Intent()
    object MouseOut : Intent()
  }

  sealed class Label {

    data class StartClick(val startDicomX: Double, val startDicomY: Double) : Label()
    data class MouseMove(val dicomX: Double, val dicomY: Double) : Label()
    data class MoveInClick(val deltaX: Double, val deltaY: Double) : Label()
    object StopMove : Label()

    data class Drawn(val circle: Circle) : Label()
    data class CenterMarkClick(val dicomX: Double, val dicomY: Double) : Label()
    data class ChangeSlice(val deltaDicomY: Int) : Label()

    data class ChangeContrastBrightness(val deltaX: Double, val deltaY: Double) : Label()

    object ContrastBrightnessChanged : Label()
  }
}
