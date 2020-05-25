package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.Grid
import view.GridView.Event
import view.GridView.Model

interface GridView : MviView<Model, Event> {

  data class Model(
    val items: List<Grid>,
    val current: Grid
  )

  sealed class Event {
    data class ItemClick(val grid: Grid) : Event()
  }
}

fun initialGridModel(): Model = Model(
  items = listOf(),
  current = Grid.Four
)
