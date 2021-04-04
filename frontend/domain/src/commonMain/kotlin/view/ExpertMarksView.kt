package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.ExpertMarkEntity
import model.ExpertQuestion
import model.ExpertQuestionsModel
import view.ExpertMarksView.Event
import view.ExpertMarksView.Model

interface ExpertMarksView : MviView<Model, Event> {

  data class Model(
    val loading: Boolean,
    val models: List<ExpertQuestionsModel>,
    val error: String
  )

  sealed class Event {
    object DismissError : Event()
    data class VariantChosen(val expertMarkEntity: ExpertMarkEntity, val question: ExpertQuestion<*>) : Event()
    data class SelectMark(val id: Int): Event()
  }
}

fun initialExpertMarksModel(): Model = Model(
  models = listOf(),
  loading = false,
  error = ""
)