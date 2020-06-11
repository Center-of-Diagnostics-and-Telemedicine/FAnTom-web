package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.Circle
import model.Cut
import model.CutType
import model.ResearchSlicesSizesData
import view.ResearchView.Event
import view.ResearchView.Model

interface ResearchView : MviView<Model, Event> {

  data class Model(
    val data: ResearchSlicesSizesData? = null,
    val error: String,
    val loading: Boolean
  )

  sealed class Event {
    data class NewArea(val circle: Circle, val sliceNumber: Int, val cut: Cut) : Event()

    object Reload : Event()
    object DismissError : Event()
    object Close : Event()
  }
}

fun initialResearchModel(): Model = Model(
  error = "",
  loading = false
)
