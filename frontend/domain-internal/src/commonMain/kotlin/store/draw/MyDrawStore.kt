package store.draw

import com.arkivanov.mvikotlin.core.store.Store
import model.*
import store.draw.MyDrawStore.*

interface MyDrawStore : Store<Intent, State, Label> {

  data class State(
    val shape: Shape? = null,
    val contrastBrightness: MouseClickPositionModel? = null,
    val mouseInClickPosition: MouseClickPositionModel? = null,
    val plane: MyPlane,
    val screenDimensionsModel: ScreenDimensionsModel,
  )

  sealed class Intent {
    data class StartDrawEllipse(val startDicomX: Double, val startDicomY: Double) : Intent()
    data class StartDrawCircle(val startDicomX: Double, val startDicomY: Double) : Intent()
    data class StartDrawRectangle(val startDicomX: Double, val startDicomY: Double) : Intent()
    data class StartContrastBrightness(val startDicomX: Double, val startDicomY: Double) : Intent()
    data class StartMouseClick(val startDicomX: Double, val startDicomY: Double) : Intent()

    data class Move(val dicomX: Double, val dicomY: Double) : Intent()
    object MouseUp : Intent()
    data class MouseWheel(val deltaDicomY: Int) : Intent()
    object MouseOut : Intent()
    object DoubleClick : Intent()

    data class UpdateScreenDimensions(val dimensions: ScreenDimensionsModel) : Intent()
  }

  sealed class Label {

    data class CircleDrawn(val circle: CircleModel) : Label()
    data class EllipseDrawn(val ellipse: EllipseModel) : Label()
    data class RectangleDrawn(val rectangle: RectangleModel) : Label()

    data class ChangeSlice(val deltaDicomY: Int) : Label()
    data class ChangeContrastBrightness(val deltaX: Double, val deltaY: Double) : Label()

    data class MousePointPosition(val mousePosition: PointPositionModel?) : Label()

    object OpenFullCut : Label()

    object ContrastBrightnessChanged : Label()
  }
}