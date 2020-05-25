package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.Tool
import store.ToolsStore.Intent
import store.ToolsStore.State

abstract class ToolsStoreAbstractFactory(
  private val storeFactory: StoreFactory
) {

  fun create(): ToolsStore =
    object : ToolsStore, Store<Intent, State, Nothing> by storeFactory.create(
      name = "ToolsStore",
      initialState = getInitialState(),
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  protected abstract fun createExecutor(): Executor<Intent, Nothing, State, Result, Nothing>

  protected sealed class Result : JvmSerializable {
    data class ToolChanged(val tool: Tool) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.ToolChanged -> copy(current = result.tool)
      }
  }

  private fun getInitialState(): State = State(
    list = listOf(Tool.MIP, Tool.Brightness, Tool.Preset)
  )
}
