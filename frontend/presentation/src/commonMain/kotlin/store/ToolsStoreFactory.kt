package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.ResearchData
import store.tools.ToolsStore.Intent
import store.tools.ToolsStore.State
import store.tools.ToolsStoreAbstractFactory

internal class ToolsStoreFactory(
  storeFactory: StoreFactory,
  data: ResearchData
) : ToolsStoreAbstractFactory(
  storeFactory = storeFactory,
  data = data
) {

  override fun createExecutor(): Executor<Intent, Nothing, State, Result, Nothing> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Nothing>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleToolClick -> dispatch(Result.ToolChanged(intent.tool))
      }.let {}
    }
  }

}
