package store.marks

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.MarkModel
import model.MarkTypeModel
import store.marks.MyMarksStore.*

interface MyMarksStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class SelectMark(val id: Int) : Intent()
    data class DeleteMark(val id: Int) : Intent()
    data class ChangeComment(val id: Int, val comment: String) : Intent()
    data class ChangeMarkType(val id: Int, val typeId: String) : Intent()
    data class ChangeVisibility(val id: Int) : Intent()

    object DismissError : Intent()
    object ReloadRequested : Intent()
  }

  data class State(
    val marks: List<MarkModel> = listOf(),
    val currentMark: MarkModel? = null,
    val markTypes: List<MarkTypeModel> = listOf(),
    val loading: Boolean = false,
    val error: String = "",
  ) : JvmSerializable

  sealed class Label {
    object CloseResearch : Label()
    data class MarksLoaded(val list: List<MarkModel>) : Label()
    data class CenterSelectedMark(val mark: MarkModel) : Label()
    data class AcceptMark(val mark: MarkModel) : Label()
  }
}