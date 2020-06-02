package store.draw

import com.arkivanov.mvikotlin.core.store.Store
import model.Circle
import model.CutType
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
      return Circle(startDicomX, startDicomY, dicomRadius)
    }
  }

  sealed class Intent {
    data class StartDraw(val startDicomX: Double, val startDicomY: Double) : Intent()
    data class StartContrastBrightness(val startDicomX: Double, val startDicomY: Double) : Intent()
    data class StartMouseMove(val startDicomX: Double, val startDicomY: Double) :
      Intent()

    data class Move(val dicomX: Double, val dicomY: Double) : Intent()
    data class MouseUp(val dicomX: Double, val dicomY: Double) : Intent()
    data class MouseClick(val dicomX: Double, val dicomY: Double, val altKey: Boolean) :
      Intent()

    data class MouseWheel(val deltaDicomY: Double) : Intent()
    object MouseOut : Intent()
  }

  sealed class Label {
    data class StartMove(val startDicomX: Double, val startDicomY: Double, val cutType: CutType) :
      Label()

    data class Drawing(val circle: Circle, val cutType: CutType) : Label()
    data class ChangeContrastBrightness(val deltaX: Double, val deltaY: Double) : Label()
    data class MouseMove(val dicomX: Double, val dicomY: Double, val cutType: CutType) :
      Label()

    data class Drawn(val circle: Circle, val cutType: CutType) : Label()
    data class OnClick(
      val dicomX: Double,
      val dicomY: Double,
      val altKey: Boolean,
      val type: CutType
    ) :
      Label()

    data class ChangeSlice(val deltaDicomY: Double, val cutType: CutType) : Label()
  }
}
