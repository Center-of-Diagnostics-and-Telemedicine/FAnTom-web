package store.marks

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Cut
import model.MarkModel
import model.MarkTypeModel
import model.Shape
import store.marks.MarksStore.*

interface MarksStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleNewMark(val shape: Shape, val sliceNumber: Int, val cut: Cut) : Intent()
    data class SelectMark(val mark: MarkModel) : Intent()
    data class UnselectMark(val mark: MarkModel) : Intent()
    data class UpdateMarkWithoutSaving(val markToUpdate: MarkModel) : Intent()
    data class UpdateMarkWithSave(val mark: MarkModel) : Intent()
    data class DeleteMark(val mark: MarkModel) : MarksStore.Intent()
    data class UpdateComment(val mark: MarkModel, val comment: String) : Intent()
    data class ChangeMarkType(val type: MarkTypeModel, val markId: Int) : Intent()
    data class SetCurrentMark(val mark: MarkModel) : Intent()

    object DismissError : Intent()
    object ReloadRequested : Intent()
    object DeleteClicked : Intent()
    object HandleCloseResearch : Intent()
  }

  data class State(
    val marks: List<MarkModel> = listOf(),
    val current: MarkModel? = null,
    val loading: Boolean = false,
    val error: String = "",
    val markTypes: List<MarkTypeModel> = listOf()
  ) : JvmSerializable

  sealed class Label {
    object CloseResearch : Label()
    data class MarksLoaded(val list: List<MarkModel>) : Label()
    data class CenterSelectedMark(val mark: MarkModel) : Label()
  }
}
