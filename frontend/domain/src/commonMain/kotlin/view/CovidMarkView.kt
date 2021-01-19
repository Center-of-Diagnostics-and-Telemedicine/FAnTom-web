package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.LungLobeModel
import view.CovidMarksView.Event
import view.CovidMarksView.Model

interface CovidMarksView : MviView<Model, Event> {

  data class Model(
    val loading: Boolean,
    val lungLobeModels: List<LungLobeModel>,
    val error: String
  )

  sealed class Event {
    data class VariantChosen(val lungLobeModel: LungLobeModel, val variant: Int) : Event()
//    data class SelectItem(val mark: MarkModel) : Event()
//    data class ItemCommentChanged(val mark: MarkModel, val comment: String) : Event()
//    data class DeleteItem(val mark: MarkModel) : Event()
//    data class ChangeMarkType(val type: MarkTypeModel, val markId: Int) : Event()
//    object DissmissError : Event()
  }
}

fun initialCovidMarksModel(): Model = Model(
  lungLobeModels = listOf(),
  loading = false,
  error = ""
)