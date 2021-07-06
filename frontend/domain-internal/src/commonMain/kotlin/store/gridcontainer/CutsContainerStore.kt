package store.gridcontainer

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Plane
import model.CutType
import model.GridModel
import store.gridcontainer.CutsContainerStore.Intent
import store.gridcontainer.CutsContainerStore.State

interface CutsContainerStore : Store<Intent, State, Nothing> {

  sealed class Intent : JvmSerializable {
    data class HandleGridChanged(val grid: GridModel) : Intent()
    data class HandleChangeCutType(val cutType: CutType, val cut: Plane) : Intent()
  }

  data class State(
    val cuts: List<Plane>,
    val grid: GridModel
  ) : JvmSerializable
}
