package store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Filter
import model.Research
import store.ListStore.Intent
import store.ListStore.State

interface ListStore : Store<Intent, State, Nothing> {

  sealed class Intent : JvmSerializable {
    data class HandleFilterChanged(val filter: Filter): Intent()
    object DismissError : Intent()
    object ReloadRequested: Intent()
  }

  data class State(
    val list: List<Research> = listOf(),
    val loading: Boolean = false,
    val error: String = ""
  ) : JvmSerializable

//  sealed class Label : JvmSerializable {
//    object Authorized : Label()
//  }
}
