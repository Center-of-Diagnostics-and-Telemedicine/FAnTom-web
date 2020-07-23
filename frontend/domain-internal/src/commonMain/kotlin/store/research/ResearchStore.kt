package store.research

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.ResearchSlicesSizesDataNew
import store.research.ResearchStore.Intent
import store.research.ResearchStore.State
import store.research.ResearchStore.Label

interface ResearchStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    object DismissError : Intent()
    object ReloadRequested : Intent()
    object CloseRequested : Intent()
  }

  data class State(
    val data: ResearchSlicesSizesDataNew? = null,
    val loading: Boolean = false,
    val error: String = ""
  ) : JvmSerializable

  sealed class Label {
    object Close : Label()
  }
}
