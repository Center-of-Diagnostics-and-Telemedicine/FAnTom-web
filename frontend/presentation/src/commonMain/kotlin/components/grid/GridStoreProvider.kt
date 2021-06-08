package components.grid

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.*
import store.tools.MyGridStore
import store.tools.MyGridStore.*

internal class GridStoreProvider(
  private val storeFactory: StoreFactory,
  private val data: ResearchData,
  private val researchId: Int,
) {

  val initialState = State(
    grid = initialFourGrid(data.type, data.doseReport, data.modalities),
    availableGrids = listOf(
      initialSingleGrid(data.type),
      initialTwoVerticalGrid(data.type),
      initialTwoHorizontalGrid(data.type),
      initialFourGrid(data.type, data.doseReport, data.modalities)
    ),
  )

  fun provide(): MyGridStore {
    return object : MyGridStore, Store<Intent, State, Label> by storeFactory.create(
      name = "MyGridStore_$researchId",
      initialState = initialState,
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }
  }

  private sealed class Result : JvmSerializable {
    data class GridChanged(val grid: MyGrid) : Result()
  }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.ChangeGrid -> changeGrid(intent.gridType)
      }.let {}
    }

    private fun changeGrid(gridType: GridType) {
      val grid = model.GridModel.build(gridType, data.type, data.doseReport, data)
      dispatch(Result.GridChanged(grid))
      publish(Label.GridChanged(grid))
    }
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.GridChanged -> copy(grid = result.grid)
      }

  }
}