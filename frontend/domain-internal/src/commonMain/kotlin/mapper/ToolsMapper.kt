package mapper

import controller.ToolsController.Output
import store.tools.BrightnessStore
import store.tools.GridStore
import store.tools.MipStore
import store.tools.PresetStore
import store.tools.ToolsStore.Intent
import store.tools.ToolsStore.State
import view.ToolsView.Event
import view.ToolsView.Model

val toolsStateToToolsModel: State.() -> Model? = {
  Model(
    items = list,
    current = current
  )
}

val toolsEventToToolsIntent: Event.() -> Intent? =
  {
    when (this) {
      is Event.ItemClick -> Intent.HandleToolClick(tool)
      Event.CloseClick -> throw NoSuchElementException("CloseClick event not implemented")
    }
  }

val toolsEventToToolsOutput: Event.() -> Output? = {
  when (this) {
    is Event.ItemClick -> null
    is Event.CloseClick -> Output.Close
  }
}

val gridLabelToToolsOutput: GridStore.Label.() -> Output? = {
  when (this) {
    is GridStore.Label.GridChanged -> Output.GridChanged(grid = item)
  }
}

val mipLabelToToolsOutput: MipStore.Label.() -> Output? = {
  when (this) {
    is MipStore.Label.MipMethodChanged -> Output.MipMethodChanged(mip = item)
    is MipStore.Label.MipValueChanged -> Output.MipValueChanged(value = value)
  }
}

val brightnessLabelToToolsOutput: BrightnessStore.Label.() -> Output? = {
  when (this) {
    is BrightnessStore.Label.BlackChanged -> Output.BlackChanged(value = value)
    is BrightnessStore.Label.WhiteChanged -> Output.WhiteChanged(value = value)
    is BrightnessStore.Label.GammaChanged -> Output.GammaChanged(value = value)
  }
}

val presetLabelToToolsOutput: PresetStore.Label.() -> Output? = {
  when (this) {
    is PresetStore.Label.PresetChanged -> Output.PresetChanged(preset = item)
  }
}
