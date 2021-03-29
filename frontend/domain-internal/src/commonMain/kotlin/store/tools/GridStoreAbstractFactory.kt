package store.tools

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.*
import store.tools.GridStore.*

abstract class GridStoreAbstractFactory(
  private val storeFactory: StoreFactory,
  data: ResearchSlicesSizesDataNew
) {

  val initialState: State = State(
    list = listOf(
      initialSingleGrid(data.type),
      initialTwoVerticalGrid(data.type),
      initialTwoHorizontalGrid(data.type),
      initialFourGrid(data.type, data.doseReport)
    ),
    current = initialFourGrid(data.type, data.doseReport),
    previous = null
  )

  fun create(): GridStore =
    object : GridStore, Store<Intent, State, Label> by storeFactory.create(
      name = "GridStore",
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
    data class GridChanged(val grid: Grid) : Result()
    data class GridChangedTemporary(val previous: Grid, val current: Grid) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.GridChanged -> copy(current = result.grid, previous = null)
        is Result.GridChangedTemporary -> copy(current = result.current, previous = result.previous)
      }

  }
}
