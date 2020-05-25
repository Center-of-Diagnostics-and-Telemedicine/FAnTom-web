package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import store.ToolsStore.Intent
import store.ToolsStore.State

internal class ToolsStoreFactory(
  storeFactory: StoreFactory
) : ToolsStoreAbstractFactory(
  storeFactory = storeFactory
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
