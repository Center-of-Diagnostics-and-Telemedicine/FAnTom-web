package store.covid

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.LungLobeModel
import store.covid.CovidMarksStore.*


interface CovidMarksStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class ChangeVariant(val lungLobeModel: LungLobeModel, val variant: Int) : Intent()
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