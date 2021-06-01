package components.researchtools

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.Tool
import store.tools.ToolsStore
import store.tools.ToolsStore.Intent
import store.tools.ToolsStore.State

internal class ResearchToolsStoreProvider(
  private val storeFactory: StoreFactory,
) {

  fun provide(): ToolsStore =
    object : ToolsStore,
      Store<Intent, State, Nothing> by storeFactory.create(
        name = "MarksStore",
        initialState = State(),
        bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = ::ExecutorImpl,
        reducer = ReducerImpl
      ) {
      init {
        ensureNeverFrozen()
      }
    }

  private sealed class Result : JvmSerializable {
    data class ToolChanged(val tool: Tool) : Result()
  }

  private inner class ExecutorImpl :
    ReaktiveExecutor<Intent, Unit, State, Result, Nothing>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleToolClick -> dispatch(Result.ToolChanged(intent.tool))
      }.let {}
    }
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.ToolChanged -> copy(current = result.tool)
      }
  }

}