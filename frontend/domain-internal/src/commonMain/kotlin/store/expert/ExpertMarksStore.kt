package store.expert

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.*
import store.expert.ExpertMarksStore.*

interface ExpertMarksStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class ChangeVariant(val markEntity: ExpertMarkEntity, val question: ExpertQuestion<*>) : Intent()

    data class ChangeVariantText(val question: ExpertQuestion<*>, val variant: String) : Intent()
    data class SelectMark(val id: Int) : Intent()
    data class AcceptMark(val mark: MarkModel) : Intent()

    object DismissError : Intent()
    object ReloadRequested : Intent()
    object HandleCloseResearch : Intent()
  }

  data class State(
    val models: List<ExpertQuestionsModel> = listOf(),
    val current: ExpertQuestionsModel? = null,
    val loading: Boolean = false,
    val error: String = ""
  ) : JvmSerializable

  sealed class Label {
    data class Marks(val models: List<ExpertQuestionsModel>) : Label()

    object CloseResearch : Label()
  }
}