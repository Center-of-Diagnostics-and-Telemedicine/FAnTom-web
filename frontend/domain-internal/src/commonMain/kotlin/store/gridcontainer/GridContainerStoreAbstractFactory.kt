package store.gridcontainer

import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.Cut
import model.Grid
import model.initialFourGrid
import store.gridcontainer.GridContainerStore.Intent
import store.gridcontainer.GridContainerStore.State

abstract class GridContainerStoreAbstractFactory(
  private val storeFactory: StoreFactory
) {

  val initialState: State = State(
    cuts = listOf(),
    grid = initialFourGrid()
  )

  fun create(): GridContainerStore =
    object : GridContainerStore, Store<Intent, State, Nothing> by storeFactory.create(
      name = "GridContainerStore",
      initialState = initialState,
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  protected abstract fun createExecutor(): Executor<Intent, Unit, State, Result, Nothing>

  protected sealed class Result : JvmSerializable {
    data class Loaded(val items: List<Cut>, val grid: Grid) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.Loaded -> copy(cuts = result.items, grid = result.grid)
      }
  }
}
