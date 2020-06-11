package store.shapes

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Cut
import model.PointPosition
import store.shapes.ShapesStore.*

interface ShapesStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleSliceNumberChange(val sliceNumber: Int) : Intent()
    data class HandleExternalSliceNumberChanged(val sliceNumber: Int, val cut: Cut) : Intent()
    data class HandleMousePosition(val dicomX: Double, val dicomY: Double) : Intent()
  }

  data class State(
    val horizontalCoefficient: Double,
    val verticalCoefficient: Double,
    val sliceNumber: Int,
    val position: PointPosition?
  ) : JvmSerializable

  sealed class Label {

  }
}
