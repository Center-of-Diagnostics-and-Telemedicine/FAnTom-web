package store.marks

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Circle
import model.Cut
import model.Mark
import store.marks.MarksStore.*

interface MarksStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleNewMark(val circle: Circle, val cut: Cut) : Intent()
    object DismissError : Intent()
    object ReloadRequested : Intent()
  }

  data class State(
    val marks: List<Mark> = listOf(),
    val loading: Boolean = false,
    val error: String = ""
  ) : JvmSerializable

  sealed class Label {
    data class MarksLoaded(val list: List<Mark>) : Label()
  }
}
