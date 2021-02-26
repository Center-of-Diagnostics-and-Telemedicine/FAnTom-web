package view

import com.arkivanov.mvikotlin.core.view.MviView
import view.CutView.Model
import view.CutView.Event

interface CutView : MviView<Model, Event> {

  data class Model(
    val slice: String,
    val sliceNumber: Int,
    val mainLoading: Boolean,
    val secondaryLoading: Boolean,
    val error: String
  )

  sealed class Event {
    object DismissError: Event()
  }
}

fun initialCutModel(): Model = Model(
  slice = "",
  sliceNumber = 1,
  mainLoading = false,
  secondaryLoading = false,
  error = ""
)
