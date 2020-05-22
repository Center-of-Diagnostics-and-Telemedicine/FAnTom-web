package store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Filter
import store.FilterStore.*

interface FilterStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleFilterClick(val filter: Filter) : Intent()
  }

  data class State(
    val list: List<Filter> = listOf(),
    val current: Filter = Filter.All
  ) : JvmSerializable

  sealed class Label : JvmSerializable {
    data class FilterChanged(val item: Filter) : Label()
  }
}
