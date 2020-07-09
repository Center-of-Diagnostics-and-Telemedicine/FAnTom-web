package store.marks

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Circle
import model.Cut
import model.MarkDomain
import store.marks.MarksStore.*

interface MarksStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleNewMark(val circle: Circle, val sliceNumber: Int, val cut: Cut) : Intent()
    data class SelectMark(val mark: MarkDomain) : Intent()
    data class UnselectMark(val mark: MarkDomain) : Intent()
    data class UpdateMark(val markToUpdate: MarkDomain) : Intent()

    object DismissError : Intent()
    object ReloadRequested : Intent()
  }

  data class State(
    val marks: List<MarkDomain> = listOf(),
    val current: MarkDomain? = null,
    val loading: Boolean = false,
    val error: String = ""
  ) : JvmSerializable

  sealed class Label {
    data class MarksLoaded(val list: List<MarkDomain>) : Label()
  }
}
