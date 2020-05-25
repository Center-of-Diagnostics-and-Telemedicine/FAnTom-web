package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.Presets
import view.PresetView.Event
import view.PresetView.Model

interface PresetView : MviView<Model, Event> {

  data class Model(
      val items: List<Presets>,
      val current: Presets
  )

  sealed class Event {
    data class ItemClick(val preset: Presets) : Event()
  }
}

fun initialPresetModel(): Model = Model(
  items = listOf(),
  current = Presets.getInitialPreset()
)
