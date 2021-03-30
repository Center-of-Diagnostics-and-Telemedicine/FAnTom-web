package store.expert

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.ExpertQuestion
import model.ExpertRoiEntity
import model.RoiExpertQuestionsModel
import store.expert.ExpertMarksStore.*

interface ExpertMarksStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class ChangeVariant(val roi: ExpertRoiEntity,val question: ExpertQuestion<*>) : Intent()

    data class ChangeVariantText(val question: ExpertQuestion<*>, val variant: String) : Intent()
    object DismissError : Intent()
    object ReloadRequested : Intent()
    object HandleCloseResearch : Intent()
  }

  data class State(
    val models: List<RoiExpertQuestionsModel> = listOf(),
    val current: RoiExpertQuestionsModel? = null,
    val loading: Boolean = false,
    val error: String = ""
  ) : JvmSerializable

  sealed class Label {
    data class Marks(val models: List<RoiExpertQuestionsModel>) : Label()

    object CloseResearch : Label()
  }
}