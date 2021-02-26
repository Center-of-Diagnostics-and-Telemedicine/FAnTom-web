package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.ResearchSlicesSizesDataNew
import view.ResearchView.Event
import view.ResearchView.Model

interface ResearchView : MviView<Model, Event> {

  data class Model(
    val data: ResearchSlicesSizesDataNew? = null,
    val error: String,
    val loading: Boolean
  )

  sealed class Event {
    object Reload : Event()
    object DismissError : Event()
    object BackToList : Event()
    object Close : Event()
  }
}

fun initialResearchModel(): Model = Model(
  error = "",
  loading = false
)
