package store.covid

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.LungLobeModel
import store.covid.CovidMarksStore.*


interface CovidMarksStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class ChangeVariant(val lungLobeModel: LungLobeModel, val variant: Int) : Intent()
//    data class HandleNewMark(val circle: Circle, val sliceNumber: Int, val cut: Cut) : Intent()
//    data class SelectMark(val mark: MarkModel) : Intent()
//    data class UnselectMark(val mark: MarkModel) : Intent()
//    data class UpdateMarkWithoutSaving(val markToUpdate: MarkModel) : Intent()
//    data class UpdateMarkWithSave(val mark: MarkModel) : Intent()
//    data class DeleteMark(val mark: MarkModel) : CovidMarksStore.Intent()
//    data class UpdateComment(val mark: MarkModel, val comment: String) : Intent()
//    data class ChangeMarkType(val type: MarkTypeModel, val markId: Int) : Intent()
//    data class SetCurrentMark(val mark: MarkModel) : Intent()

    object DismissError : Intent()
    object ReloadRequested : Intent()
    object DeleteClicked : Intent()
    object HandleCloseResearch : Intent()
  }

  data class State(
    val covidLungLobes: List<LungLobeModel>,
    val loading: Boolean = false,
    val error: String = ""
  ) : JvmSerializable

  sealed class Label {
    object CloseResearch : Label()
  }
}