package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.LungLobeModel
import view.CovidMarksView.Event
import view.CovidMarksView.Model

interface CovidMarksView : MviView<Model, Event> {

  data class Model(
    val loading: Boolean,
    val lungLobeModels: Map<Int, LungLobeModel>,
    val error: String
  )

  sealed class Event {
    data class VariantChosen(val lungLobeModel: LungLobeModel, val variant: Int) : Event()
  }
}

fun initialCovidMarksModel(): Model = Model(
  lungLobeModels = mapOf(),
  loading = false,
  error = ""
)