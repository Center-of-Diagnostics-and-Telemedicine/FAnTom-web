package store.draw

import com.arkivanov.mvikotlin.core.store.Store
import model.Circle
import model.CutType
import model.Rectangle
import model.defaultMarkColor
import store.draw.DrawStore.*
import kotlin.math.abs

interface DrawStore : Store<Intent, State, Label> {

  data class State(
    val startDicomX: Double,
    val startDicomY: Double,
    val dicomRadiusHorizontal: Double,
    val dicomRadiusVertical: Double,
    val isDrawingEllipse: Boolean = false,
    val isDrawingRectangle: Boolean = false,
    val isMoving: Boolean = false,
    val isContrastBrightness: Boolean = false,
    val cutType: CutType,
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
          isCenter = false,
          color = defaultMarkColor
        )
      } else {
        Circle(
          dicomCenterX = startDicomX,
          dicomCenterY = startDicomY,
          dicomRadiusHorizontal = dicomRadiusHorizontal,
          dicomRadiusVertical = dicomRadiusVertical,
          id = -1,
          highlight = false,
          isCenter = false,
          color = defaultMarkColor
        )
      }
    }

    fun rectangle(): Rectangle {
      val horizontalRadius = dicomRadiusHorizontal / 2
      val verticalRadius = dicomRadiusVertical / 2
      return Rectangle(
        dicomCenterX = startDicomX + horizontalRadius,
        dicomCenterY = startDicomY + verticalRadius,
        dicomRadiusHorizontal = abs(horizontalRadius),
        dicomRadiusVertical = abs(verticalRadius),
        id = -1,
        highlight = false,
        isCenter = false,
        color = defaultMarkColor
      )
    }
  }

  sealed class Intent {
    data class StartDrawEllipse(val startDicomX: Double, val startDicomY: Double) : Intent()
    data class StartDrawRectangle(val startDicomX: Double, val startDicomY: Double) : Intent()
    data class StartContrastBrightness(val startDicomX: Double, val startDicomY: Double) : Intent()
    data class StartMouseClick(val startDicomX: Double, val startDicomY: Double) : Intent()

    data class Move(val dicomX: Double, val dicomY: Double) : Intent()
    object MouseUp : Intent()
    data class MouseWheel(val deltaDicomY: Int) : Intent()
    object MouseOut : Intent()
    object DoubleClick : Intent()
  }

  sealed class Label {

    data class StartClick(val startDicomX: Double, val startDicomY: Double) : Label()
    data class MouseMove(val dicomX: Double, val dicomY: Double) : Label()
    data class MoveInClick(val deltaX: Double, val deltaY: Double) : Label()
    object StopMove : Label()

    data class CircleDrawn(val circle: Circle) : Label()
    data class RectangleDrawn(val rectangle: Rectangle) : Label()

    data class ChangeSlice(val deltaDicomY: Int) : Label()

    data class ChangeContrastBrightness(val deltaX: Double, val deltaY: Double) : Label()

    object OpenFullCut : Label()

    object ContrastBrightnessChanged : Label()
  }
}
