package store.shapes

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Circle
import model.Cut
import model.MarkDomain
import model.PointPosition
import store.shapes.ShapesStore.*

interface ShapesStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleSliceNumberChange(val sliceNumber: Int) : Intent()
    data class HandleExternalSliceNumberChanged(val sliceNumber: Int, val cut: Cut) : Intent()
    data class HandleMousePosition(val dicomX: Double, val dicomY: Double) : Intent()
    data class HandleMarks(val list: List<MarkDomain>) : Intent()
    data class HandleClick(val dicomX: Double, val dicomY: Double, val altKey: Boolean) : Intent()
  }

  data class State(
    val horizontalCoefficient: Double,
    val verticalCoefficient: Double,
    val sliceNumber: Int,
    val position: PointPosition?,
    val circles: List<Circle>,
    val hounsfield: Int?,
    val marks: List<MarkDomain>
  ) : JvmSerializable

  sealed class Label {

  }
}
