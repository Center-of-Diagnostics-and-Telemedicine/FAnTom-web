package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.ExpertQuestion
import model.ExpertRoiEntity
import model.RoiExpertQuestionsModel
import view.ExpertMarksView.Event
import view.ExpertMarksView.Model

interface ExpertMarksView : MviView<Model, Event> {

  data class Model(
          val loading: Boolean,
          val models: List<RoiExpertQuestionsModel>,
          val error: String
  )

  sealed class Event {
    object DismissError : Event()
    data class VariantChosen(val roi: ExpertRoiEntity, val question: ExpertQuestion<*>) : Event()
  }
}

fun initialExpertMarksModel(): Model = Model(
  models = listOf(),
  loading = false,
  error = ""
)