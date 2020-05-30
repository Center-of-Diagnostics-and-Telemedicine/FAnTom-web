package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.Presets
import store.tools.PresetStore.*
import store.tools.PresetStoreAbstractFactory

internal class PresetStoreFactory(
  storeFactory: StoreFactory
) : PresetStoreAbstractFactory(
  storeFactory = storeFactory
) {

  override fun createExecutor(): Executor<Intent, Nothing, State, Result, Label> =
    object : ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

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
