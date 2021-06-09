package store.gridcontainer

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Cut
import model.GridModel
import model.GridType
import store.gridcontainer.MyCutsContainerStore.Intent
import store.gridcontainer.MyCutsContainerStore.State

interface MyCutsContainerStore : Store<Intent, State, Nothing> {

  sealed class Intent : JvmSerializable {
  }

  data class State(
    val gridType: GridType
  ) : JvmSerializable
}
