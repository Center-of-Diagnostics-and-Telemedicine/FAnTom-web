package store.tools

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.MyNewGrid
import model.MyNewGridType
import store.tools.MyGridStore.*

interface MyGridStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class ChangeGrid(val gridType: MyNewGridType) : Intent()
    data class ChangeSeries(val series: String) : Intent()
  }

  data class State(
    val grid: MyNewGrid,
    val series: List<String>,
    val currentSeries: String
  ) : JvmSerializable

  sealed class Label : JvmSerializable {
    data class GridChanged(val item: MyNewGrid) : Label()
  }
}