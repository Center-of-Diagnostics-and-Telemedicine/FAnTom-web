package store.tools

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Cut
import model.Grid
import model.GridType
import store.tools.GridStore.*

interface GridStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleGridClick(val gridType: GridType) : Intent()
    data class HandleOpenFullCut(val cut: Cut) : Intent()
    data class HandleReturnPreviousGrid(val cut: Cut) : Intent()
  }

  data class State(
    val list: List<Grid> = listOf(),
    val current: Grid,
    val previous: Grid?
  ) : JvmSerializable

  sealed class Label : JvmSerializable {
    data class GridChanged(val item: Grid) : Label()
  }
}
