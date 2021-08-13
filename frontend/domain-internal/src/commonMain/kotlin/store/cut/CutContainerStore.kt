package store.cut

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.*
import store.cut.CutContainerStore.*

interface CutContainerStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class ChangeSliceNumber(val sliceNumber: Int) : Intent()
    data class HandleNewShape(val shape: Shape) : Intent()
    data class UpdateScreenDimensions(val dimensions: ScreenDimensionsModel) : Intent()
    data class PointPosition(val pointPosition: PointPositionModel?) : Intent()
  }

  data class State(
    val cutModel: CutModel,
    val marks: List<MarkModel>,
    val mark: MarkModel?,
    val screenDimensionsModel: ScreenDimensionsModel,
    val pointPosition: PointPosition?,
  ) : JvmSerializable


  sealed class Label {
    data class CutModelChanged(val cutModel: CutModel) : Label()
    data class Shapes(val shapes: List<Shape>) : Label()
    data class MousePosition(val pointPosition: PointPosition?) : Label()
    data class ScreenDimensions(val dimensions: ScreenDimensionsModel) : Label()
  }
}

data class CutModel(
  val sliceNumber: Int,
  val blackValue: Int,
  val whiteValue: Int,
  val gammaValue: Double,
  val mip: Mip,
  val width: Int,
  val height: Int
)

