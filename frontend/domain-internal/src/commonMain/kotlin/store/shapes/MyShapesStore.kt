package store.shapes

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.*
import store.shapes.MyShapesStore.*

interface MyShapesStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
  }

  data class State(
    val shapes: List<Shape>,
    val sliceNumber: Int,
    val position: PointPosition?,
    val hounsfield: Int?,
    val cutType: CutType,
    val plane: Plane
  ) : JvmSerializable

  sealed class Label {
    data class SelectMark(val mark: MarkModel) : Label()
    data class UnselectMark(val mark: MarkModel) : Label()
    data class UpdateMarkCoordinates(val mark: MarkModel) : Label()
    data class UpdateMarkWithSave(val mark: MarkModel) : Label()
    data class ChangeCutType(val cutType: CutType) : Label()
  }
}
