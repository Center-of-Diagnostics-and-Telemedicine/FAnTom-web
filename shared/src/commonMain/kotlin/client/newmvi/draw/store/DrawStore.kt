package client.newmvi.draw.store

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import model.Circle
import model.ResearchSlicesSizesData

interface DrawStore : Disposable {

  val states: Observable<State>

  fun accept(intent: Intent)

  data class State(
    val startX: Double,
    val startY: Double,
    val radius: Double,
    val isDrawing: Boolean = false,
    val isMoving: Boolean = false,
    val isContrastBrightness: Boolean = false,
    val sliceSizeData: ResearchSlicesSizesData? = null
  ) {
    fun circle(): Circle {
      return Circle(startX, startY, radius)
    }
  }

  sealed class Intent {
    class StartDraw(val startX: Double, val startY: Double) : Intent()
    class StartContrastBrightness(val startX: Double, val startY: Double) : Intent()
    class StartMouseMove(val startY: Double, val startX: Double, val shiftKey: Boolean) : Intent()

    class Move(val x: Double, val y: Double) : Intent()
    class MouseUp(val x: Double, val y: Double) : Intent()
    object MouseOut : Intent()
    class MouseClick(val x: Double, val y: Double, val altKey: Boolean) : Intent()

    class ChangeSliceNumber(val deltaY: Int) : Intent()

    object None : Intent()
  }
}