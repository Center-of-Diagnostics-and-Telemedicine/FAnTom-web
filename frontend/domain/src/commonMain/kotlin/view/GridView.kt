package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.Grid
import model.GridType
import model.initialFourGrid
import view.GridView.Event
import view.GridView.Model

interface GridView : MviView<Model, Event> {

  data class Model(
    val items: List<Grid>,
    val current: Grid
  )

  sealed class Event {
    data class ItemClick(val gridType: GridType) : Event()
  }
}

fun initialGridModel(): Model = Model(
  items = listOf(),
  current = initialFourGrid()
)
