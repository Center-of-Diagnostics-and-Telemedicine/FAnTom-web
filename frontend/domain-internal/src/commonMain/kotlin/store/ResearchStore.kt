package store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.ResearchSlicesSizesData
import store.ResearchStore.Intent
import store.ResearchStore.State

interface ResearchStore : Store<Intent, State, Nothing> {

  sealed class Intent : JvmSerializable {
    object DismissError : Intent()
    object ReloadRequested : Intent()
    object CloseRequested : Intent()
  }

  data class State(
    val data: ResearchSlicesSizesData? = null,
    val loading: Boolean = false,
    val error: String = ""
  ) : JvmSerializable

  sealed class Output {
    object Close : Output()
  }
}
