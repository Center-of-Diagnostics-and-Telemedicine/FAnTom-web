package client.newmvi.menu.preset.binder

import client.*
import client.newmvi.menu.preset.store.PresetStore
import client.newmvi.menu.preset.view.PresetView
import model.Preset
import model.*

internal object PresetViewEventToIntentMapper {

  operator fun invoke(event: PresetView.Event): PresetStore.Intent =
    when (event) {
      is PresetView.Event.ChangeValue -> PresetStore.Intent.ChangePreset(getPreset(event.value))
    }

  private fun getPreset(value: String): Preset {
    return when (value) {
      SOFT_TISSUE -> Preset.PRESET_SOFT_TISSUE
      VESSELS -> Preset.PRESET_VESSELS
      BONES -> Preset.PRESET_BONES
      BRAIN -> Preset.PRESET_BRAIN
      LUNGS -> Preset.PRESET_LUNGS
      else -> TODO("not implemented")
    }
  }
}