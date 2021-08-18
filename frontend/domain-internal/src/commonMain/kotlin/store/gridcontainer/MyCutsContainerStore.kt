package store.gridcontainer

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.MyNewGrid
import store.gridcontainer.MyCutsContainerStore.*

interface MyCutsContainerStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
  }

  data class State(
    val grid: MyNewGrid? = null,
    val loading: Boolean = false,
    val error: String? = null
  ) : JvmSerializable

  sealed class Label {
    data class GridTypeChanged(val grid: MyNewGrid) : Label()
  }
}
