package store.expert

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.ExpertQuestion
import model.RoiExpertQuestionsModel
import store.expert.ExpertMarksStore.*

interface ExpertMarksStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class ChangeVariant<VariantType>(
      val question: ExpertQuestion<*>,
      val variant: VariantType
    ) : Intent()

    data class ChangeVariantText(val question: ExpertQuestion<*>, val variant: String) : Intent()
    object DismissError : Intent()
    object ReloadRequested : Intent()
    object HandleCloseResearch : Intent()
  }

  data class State(
    val roisQuestions: List<RoiExpertQuestionsModel> = listOf(),
    val current: RoiExpertQuestionsModel? = null,
    val loading: Boolean = false,
    val error: String = ""
  ) : JvmSerializable

  sealed class Label {
    object CloseResearch : Label()
  }
}