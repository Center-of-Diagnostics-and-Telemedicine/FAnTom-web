package store.shapes

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.*
import store.shapes.MyShapesStore.*

interface MyShapesStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleMousePosition(val dicomX: Double, val dicomY: Double) : Intent()
    data class HandleSliceNumberChange(val sliceNumber: Int) : Intent()
    data class HandleShapes(val shapes: List<Shape>) : Intent()
    data class UpdateScreenDimensions(val dimensions: ScreenDimensionsModel) : Intent()
  }

  data class State(
    val shapes: List<Shape>,
    val sliceNumber: Int,
    val position: PointPosition?,
    val hounsfield: Int?,
    val cutType: CutType,
    val plane: Plane,
    val screenDimensionsModel: ScreenDimensionsModel,
  ) : JvmSerializable

  sealed class Label {
    data class ChangeCutType(val cutType: CutType) : Label()
  }
}