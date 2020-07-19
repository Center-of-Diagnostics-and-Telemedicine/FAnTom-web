package store.draw

import com.arkivanov.mvikotlin.core.store.Store
import model.Circle
import store.draw.DrawStore.*
import kotlin.math.abs

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
        val horizontalRadius = dicomRadiusHorizontal / 2
        val verticalRadius = dicomRadiusVertical / 2
        Circle(
          dicomCenterX = startDicomX + horizontalRadius,
          dicomCenterY = startDicomY + verticalRadius,
          dicomRadiusHorizontal = abs(horizontalRadius),
          dicomRadiusVertical = abs(verticalRadius),
          id = -1,
          highlight = false,
          isCenter = false
        )
      } else {
        Circle(
          dicomCenterX = startDicomX,
          dicomCenterY = startDicomY,
          dicomRadiusHorizontal = dicomRadiusHorizontal,
          dicomRadiusVertical = dicomRadiusVertical,
          id = -1,
          highlight = false,
          isCenter = false
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
