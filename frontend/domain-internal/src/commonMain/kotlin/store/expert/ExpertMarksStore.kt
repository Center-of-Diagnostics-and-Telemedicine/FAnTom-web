package store.expert

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.ExpertQuestion
import model.MarkModel
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
    val marks: List<MarkModel> = listOf(),
    val current: Pair<MarkModel, List<ExpertQuestion<*>>>? = null,
    val loading: Boolean = false,
    val error: String = ""
  ) : JvmSerializable

  sealed class Label {
    object CloseResearch : Label()
  }
}