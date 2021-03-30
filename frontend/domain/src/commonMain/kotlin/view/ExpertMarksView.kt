package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.ExpertQuestion
import model.RoiExpertQuestionsModel
import view.ExpertMarksView.Event
import view.ExpertMarksView.Model

interface ExpertMarksView : MviView<Model, Event> {

  data class Model(
    val loading: Boolean,
    val rois: List<RoiExpertQuestionsModel>,
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
  rois = listOf(),
  loading = false,
  error = ""
)