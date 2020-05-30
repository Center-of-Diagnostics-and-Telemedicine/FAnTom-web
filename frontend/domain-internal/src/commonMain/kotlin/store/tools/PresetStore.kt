package store.tools

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Presets
import store.tools.PresetStore.*

interface PresetStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandlePresetClick(val preset: Presets) : Intent()
  }

  data class State(
    val list: List<Presets> = listOf(),
    val current: Presets = Presets.getInitialPreset()
  ) : JvmSerializable

  sealed class Label : JvmSerializable {
    data class PresetChanged(val item: Presets) : Label()
  }
}
