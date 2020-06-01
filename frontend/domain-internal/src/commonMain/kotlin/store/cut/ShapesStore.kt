package store.cut

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Cut
import store.cut.ShapesStore.Intent
import store.cut.ShapesStore.State

interface ShapesStore : Store<Intent, State, Nothing> {

  sealed class Intent : JvmSerializable {
    data class HandleSliceNumberChange(val sliceNumber: Int) : Intent()
    data class HandleExternalSliceNumberChanged(val sliceNumber: Int, val cut: Cut) : Intent()
  }

  data class State(
    val horizontalCoefficient: Double,
    val verticalCoefficient: Double,
    val sliceNumber: Int
  ) : JvmSerializable
}
