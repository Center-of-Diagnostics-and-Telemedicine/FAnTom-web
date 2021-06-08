package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.Presets
import model.ResearchData
import repository.BrightnessRepository
import store.tools.PresetStore.*
import store.tools.PresetStoreAbstractFactory

internal class PresetStoreFactory(
  storeFactory: StoreFactory,
  data: ResearchData,
  private val brightnessRepository: BrightnessRepository
) : PresetStoreAbstractFactory(
  storeFactory = storeFactory,
  data = data
) {

  override fun createExecutor(): Executor<Intent, Unit, State, Result, Label> =
    object : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

      override fun executeAction(action: Unit, getState: () -> State) {
        val currentPreset = getState().current
        brightnessRepository.setWhiteValue(currentPreset.white)
        brightnessRepository.setBlackValue(currentPreset.black)
      }

      override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
          is Intent.HandlePresetClick -> changeFilter(intent.preset)
        }.let {}
      }

      private fun changeFilter(preset: Presets) {
        dispatch(Result.PresetChanged(preset))
        publish(Label.PresetChanged(preset))
      }
    }
}
