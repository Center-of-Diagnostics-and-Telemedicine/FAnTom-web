package store.list

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.Category
import model.Filter
import model.allCategories
import model.allFilters
import store.list.FilterStore.*

abstract class FilterStoreAbstractFactory(
  private val storeFactory: StoreFactory
) {

  val initialState: State = State(
    filters = allFilters,
    currentFilter = Filter.All,
    categories = allCategories,
    currentCategory = Category.All
  )

  fun create(): FilterStore =
    object : FilterStore, Store<Intent, State, Label> by storeFactory.create(
      name = "FilterStore",
      initialState = initialState,
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  protected abstract fun createExecutor(): Executor<Intent, Nothing, State, Result, Label>
  protected sealed class Result : JvmSerializable {
    data class FilterChanged(val filter: Filter) : Result()
    data class CategoryChanged(val category: Category) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.FilterChanged -> copy(currentFilter = result.filter)
        is Result.CategoryChanged -> copy(currentCategory = result.category)
      }
  }
}
