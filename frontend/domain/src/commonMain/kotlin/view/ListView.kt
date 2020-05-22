package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.Research
import view.ListView.Event
import view.ListView.Model

interface ListView : MviView<Model, Event> {

  data class Model(
    val items: List<Research>,
    val error: String,
    val loading: Boolean
  )

  sealed class Event {
    object Reload : Event()
    data class ItemClick(val id: String): Event()
    object DismissError : Event()
  }
}

fun initialListModel(): Model = Model(
  items = listOf(),
  error = "",
  loading = false
)