package store.tools

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.GridType
import model.MyGrid
import store.tools.MyGridStore.*

interface MyGridStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class ChangeGrid(val gridType: GridType) : Intent()
  }

  data class State(
    val grid: GridType
  ) : JvmSerializable

  sealed class Label : JvmSerializable {
    data class GridChanged(val item: GridType) : Label()
  }
}