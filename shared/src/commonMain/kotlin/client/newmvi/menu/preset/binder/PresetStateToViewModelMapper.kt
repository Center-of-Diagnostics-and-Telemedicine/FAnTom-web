package client.newmvi.menu.preset.binder

import client.*
import client.newmvi.menu.preset.store.PresetStore
import client.newmvi.menu.preset.view.PresetView
import model.Preset
import model.*

internal object PresetStateToViewModelMapper {

  operator fun invoke(state: PresetStore.State): PresetView.PresetViewModel =
    PresetView.PresetViewModel(
      isLoading = state.isLoading,
      error = state.error,
      value = getName(state.value)
    )

  private fun getName(value: Preset): String {
    return when (value) {
      Preset.PRESET_SOFT_TISSUE -> SOFT_TISSUE
      Preset.PRESET_VESSELS -> VESSELS
      Preset.PRESET_BONES -> BONES
      Preset.PRESET_BRAIN -> BRAIN
      Preset.PRESET_LUNGS -> LUNGS
    }
  }
}