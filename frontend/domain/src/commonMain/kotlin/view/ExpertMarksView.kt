package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.ExpertQuestion
import model.MarkModel
import view.ExpertMarksView.Event
import view.ExpertMarksView.Model

interface ExpertMarksView : MviView<Model, Event> {

  data class Model(
    val loading: Boolean,
    val questions: List<ExpertQuestion<*>>?,
    val mark: MarkModel?,
    val error: String
  )

  sealed class Event {
    object DismissError : Event()
    data class VariantChosen<VariantType>(
      val question: ExpertQuestion<VariantType>,
      val variant: VariantType
    ) : Event()
  }
}

fun initialExpertMarksModel(): Model = Model(
  questions = listOf(),
  mark = null,
  loading = false,
  error = ""
)