package store.tools

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Plane
import model.GridModel
import model.GridType
import store.tools.GridStore.*

interface GridStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleGridClick(val gridType: GridType) : Intent()
    data class HandleOpenFullCut(val cut: Plane) : Intent()
    data class HandleReturnPreviousGrid(val cut: Plane) : Intent()
  }

  data class State(
    val list: List<GridModel> = listOf(),
    val current: GridModel,
    val previous: GridModel?
  ) : JvmSerializable

  sealed class Label : JvmSerializable {
    data class GridChanged(val item: GridModel) : Label()
  }
}
