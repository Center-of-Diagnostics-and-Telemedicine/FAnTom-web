package store.shapes

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Circle
import model.Cut
import model.CutType
import model.PointPosition
import store.shapes.ShapesStore.Intent
import store.shapes.ShapesStore.State

interface ShapesStore : Store<Intent, State, Nothing> {

  sealed class Intent : JvmSerializable {
    data class HandleSliceNumberChange(val sliceNumber: Int) : Intent()
    data class HandleExternalSliceNumberChanged(val sliceNumber: Int, val cut: Cut) : Intent()
    data class HandleMousePosition(val dicomX: Double, val dicomY: Double, val cutType: CutType) :
      Intent()

    class HandleDrawing(val circle: Circle, val cutType: CutType) : Intent()
  }

  data class State(
    val horizontalCoefficient: Double,
    val verticalCoefficient: Double,
    val sliceNumber: Int,
    val position: PointPosition?
  ) : JvmSerializable
}
