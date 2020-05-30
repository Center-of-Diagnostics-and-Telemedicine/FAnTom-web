package mapper

import store.tools.PresetStore.Intent
import store.tools.PresetStore.State
import  view.PresetView.Event
import  view.PresetView.Model

val presetStateToPresetModel: State.() -> Model? = {
  Model(
    items = list,
    current = current
  )
}

val presetEventToPresetIntent: Event.() -> Intent? =
  {
    when (this) {
      is Event.ItemClick -> Intent.HandlePresetClick(preset)
    }
  }
