package store.list

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Category
import model.Filter
import model.Research
import store.list.ListStore.Intent
import store.list.ListStore.State

interface ListStore : Store<Intent, State, Nothing> {

  sealed class Intent : JvmSerializable {
    data class HandleFilterChanged(val filter: Filter, val category: Category) : Intent()
    object DismissError : Intent()
    object ReloadRequested : Intent()
  }

  data class State(
    val list: List<Research> = listOf(),
    val loading: Boolean = false,
    val error: String = ""
  ) : JvmSerializable
}
