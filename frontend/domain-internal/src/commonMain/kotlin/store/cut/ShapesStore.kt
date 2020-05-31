package store.cut

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import store.cut.ShapesStore.Intent
import store.cut.ShapesStore.State

interface ShapesStore : Store<Intent, State, Nothing> {

  sealed class Intent : JvmSerializable {
    data class HandleHorizontalLineChanged(val line: Int) : Intent()
    data class HandleVerticalLineChanged(val line: Int) : Intent()
    data class HandleSliceNumberChange(val sliceNumber: Int) : Intent()
  }

  data class State(
    val horizontalLine: Int,
    val verticalLine: Int,
    val sliceNumber: Int
  ) : JvmSerializable
}
