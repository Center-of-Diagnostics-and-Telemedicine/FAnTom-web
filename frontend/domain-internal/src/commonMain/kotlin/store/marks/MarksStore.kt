package store.marks

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Circle
import model.Cut
import model.MarkModel
import model.MarkTypeModel
import store.marks.MarksStore.*

interface MarksStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleNewMark(val circle: Circle, val sliceNumber: Int, val cut: Cut) : Intent()
    data class SelectMark(val mark: MarkModel) : Intent()
    data class UnselectMark(val mark: MarkModel) : Intent()
    data class UpdateMarkWightoutSaving(val markToUpdate: MarkModel) : Intent()
    data class UpdateMarkWithSave(val mark: MarkModel) : Intent()
    data class DeleteMark(val mark: MarkModel) : MarksStore.Intent()
    data class UpdateComment(val mark: MarkModel, val comment: String) : Intent()
    data class ChangeMarkType(val type: MarkTypeModel, val markId: Int) : Intent()

    object DismissError : Intent()
    object ReloadRequested : Intent()
    object DeleteClicked : Intent()
  }

  data class State(
    val marks: List<MarkModel> = listOf(),
    val current: MarkModel? = null,
    val loading: Boolean = false,
    val error: String = "",
    val markTypes: List<MarkTypeModel> = listOf()
  ) : JvmSerializable

  sealed class Label {
    data class MarksLoaded(val list: List<MarkModel>) : Label()
  }
}
