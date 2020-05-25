package store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Tool
import store.ToolsStore.Intent
import store.ToolsStore.State

interface ToolsStore : Store<Intent, State, Nothing> {

  sealed class Intent : JvmSerializable {
    data class HandleToolClick(val tool: Tool) : Intent()
  }

  data class State(
    val list: List<Tool> = listOf(),
    val current: Tool? = null
  ) : JvmSerializable
}
