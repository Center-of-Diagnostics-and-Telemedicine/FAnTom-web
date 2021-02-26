package store.list

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Category
import model.Filter
import store.list.FilterStore.*

interface FilterStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleFilterClick(val filter: Filter) : Intent()
    data class HandleCategoryClick(val category: Category) : Intent()
  }

  data class State(
    val filters: List<Filter> = listOf(),
    val currentFilter: Filter = Filter.All,
    val categories: List<Category> = listOf(),
    val currentCategory: Category = Category.All
  ) : JvmSerializable

  sealed class Label : JvmSerializable {
    data class FilterChanged(val filter: Filter, val category: Category) : Label()
  }
}
