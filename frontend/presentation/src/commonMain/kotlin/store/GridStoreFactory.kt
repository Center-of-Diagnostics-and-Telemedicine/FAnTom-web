package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.Grid
import model.GridType
import model.ResearchSlicesSizesDataNew
import store.tools.GridStore.*
import store.tools.GridStoreAbstractFactory

internal class GridStoreFactory(
  storeFactory: StoreFactory,
  private val data: ResearchSlicesSizesDataNew
) : GridStoreAbstractFactory(
  storeFactory = storeFactory,
  data = data
) {

  override fun createExecutor():
    Executor<Intent, Nothing, State, Result, Label> = ExecutorImpl()

  private inner class ExecutorImpl :
    ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleGridClick -> changeFilter(intent.gridType)
      }.let {}
    }

    private fun changeFilter(gridType: GridType) {
      val grid = Grid.build(gridType, data.type)
      dispatch(Result.GridChanged(grid))
      publish(Label.GridChanged(grid))
    }
  }
}
