package store.gridcontainer

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.GridModel
import model.GridType
import store.gridcontainer.MyCutsContainerStore.*

interface MyCutsContainerStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
  }

  data class State(
    val gridType: GridType,
    val gridModel: GridModel
  ) : JvmSerializable

  sealed class Label {
    data class GridTypeChanged(val gridType: GridType) : Label()
  }
}
