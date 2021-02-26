package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.Filter
import view.FilterView.Event
import view.FilterView.Model

interface FilterView : MviView<Model, Event> {

  data class Model(
    val items: List<Filter>,
    val current: Filter
  )

  sealed class Event {
    data class ItemClick(val filter: Filter) : Event()
  }
}

fun initialFilterModel(): Model = Model(
  items = listOf(),
  current = Filter.All
)
