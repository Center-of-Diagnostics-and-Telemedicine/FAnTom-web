package store.cut

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import store.cut.MyCutStore.*

interface MyCutStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleChangeCutModel(val cutModel: CutModel) : Intent()
  }

  data class State(
    val cutModel: CutModel?,
    val slice: String = "",
    val loading: Boolean = false,
    val error: String = ""
  ) : JvmSerializable

  sealed class Label {
  }
}