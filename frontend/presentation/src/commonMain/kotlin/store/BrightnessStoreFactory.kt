package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import repository.BrightnessRepository
import store.tools.BrightnessStore.*
import store.tools.BrightnessStoreAbstractFactory

internal class BrightnessStoreFactory(
  storeFactory: StoreFactory,
  brightnessRepository: BrightnessRepository
) : BrightnessStoreAbstractFactory(
  storeFactory = storeFactory,
  brightnessRepository = brightnessRepository
) {

  override fun createExecutor(): Executor<Intent, Nothing, State, Result, Label> =
    object : ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

      override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
          is Intent.HandleBlackChanged -> handleBlackChanged(intent.value)
          is Intent.HandleWhiteValueChanged -> handleWhiteChanged(intent.value)
          is Intent.HandleGammaValueChanged -> handleGammaChanged(intent.value)
          is Intent.HandleContrastBrightnessChanged -> {
            handleBlackChanged(intent.black)
            handleWhiteChanged(intent.white)
          }
          is Intent.PresetChanged -> {
            dispatch(Result.BlackValueChanged(intent.item.black))
            dispatch(Result.WhiteValueChanged(intent.item.white))
          }
        }.let {}
      }

      private fun handleBlackChanged(value: Int) {
        brightnessRepository.setBlackValue(value)
        dispatch(Result.BlackValueChanged(value))
        publish(Label.BlackChanged(value))
      }

      private fun handleWhiteChanged(value: Int) {
        brightnessRepository.setWhiteValue(value)
        dispatch(Result.WhiteValueChanged(value))
        publish(Label.WhiteChanged(value))
      }

      private fun handleGammaChanged(value: Double) {
        brightnessRepository.setGammaValue(value)
        dispatch(Result.GammaValueChanged(value))
        publish(Label.GammaChanged(value))
      }
    }
}
