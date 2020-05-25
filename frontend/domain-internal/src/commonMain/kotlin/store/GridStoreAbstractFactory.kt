package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.Grid
import store.GridStore.*

abstract class GridStoreAbstractFactory(
  private val storeFactory: StoreFactory
) {

  fun create(): GridStore =
    object : GridStore, Store<Intent, State, Label> by storeFactory.create(
      name = "GridStore",
      initialState = getInitialState(),
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  protected abstract fun createExecutor(): Executor<Intent, Nothing, State, Result, Label>

  protected sealed class Result : JvmSerializable {
    data class GridChanged(val grid: Grid) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.GridChanged -> copy(current = result.grid)
      }
  }

  private fun getInitialState(): State = State(
    list = listOf(Grid.Single, Grid.TwoVertical, Grid.TwoHorizontal, Grid.Four),
    current = Grid.Four
  )
}
