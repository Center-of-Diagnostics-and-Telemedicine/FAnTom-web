package store.gridcontainer

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Cut
import model.Grid
import store.gridcontainer.CutsContainerStore.Intent
import store.gridcontainer.CutsContainerStore.State

interface CutsContainerStore : Store<Intent, State, Nothing> {

  sealed class Intent : JvmSerializable {
    data class HandleGridChanged(val grid: Grid) : Intent()
  }

  data class State(
    val cuts: List<Cut>,
    val grid: Grid
  ) : JvmSerializable
}
