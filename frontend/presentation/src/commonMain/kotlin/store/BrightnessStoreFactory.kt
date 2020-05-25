package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import store.BrightnessStore.*

internal class BrightnessStoreFactory(
  storeFactory: StoreFactory
) : BrightnessStoreAbstractFactory(
  storeFactory = storeFactory
) {

  override fun createExecutor(): Executor<Intent, Nothing, State, Result, Label> =
    object : ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

      override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
          is Intent.HandleBlackChanged -> handleBlackChanged(intent.value)
          is Intent.HandleWhiteValueChanged -> handleWhiteChanged(intent.value)
          is Intent.HandleGammaValueChanged -> handleGammaChanged(intent.value)
        }.let {}
      }

      private fun handleBlackChanged(value: Int) {
        dispatch(Result.BlackValueChanged(value))
        publish(Label.BlackChanged(value))
      }

      private fun handleWhiteChanged(value: Int) {
        dispatch(Result.WhiteValueChanged(value))
        publish(Label.WhiteChanged(value))
      }

      private fun handleGammaChanged(value: Double) {
        dispatch(Result.GammaValueChanged(value))
        publish(Label.GammaChanged(value))
      }
    }
}
