package store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Grid
import store.GridStore.*

interface GridStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleGridClick(val grid: Grid) : Intent()
  }

  data class State(
    val list: List<Grid> = listOf(),
    val current: Grid = Grid.Four
  ) : JvmSerializable

  sealed class Label : JvmSerializable {
    data class GridChanged(val item: Grid) : Label()
  }
}
