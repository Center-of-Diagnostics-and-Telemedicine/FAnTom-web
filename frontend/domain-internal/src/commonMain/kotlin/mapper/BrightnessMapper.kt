package mapper

import store.tools.BrightnessStore.Intent
import store.tools.BrightnessStore.State
import view.BrightnessView.Event
import view.BrightnessView.Model

val brightnessStateToBrightnessModel: State.() -> Model? = {
  Model(
    blackValue = blackValue,
    whiteValue = whiteValue,
    gammaValue = gammaValue
  )
}

val brightnessEventToBrightnessIntent: Event.() -> Intent? =
  {
    when (this) {
      is Event.BlackChanged -> Intent.HandleBlackChanged(value)
      is Event.WhiteChanged -> Intent.HandleWhiteValueChanged(value)
      is Event.GammaChanged -> Intent.HandleGammaValueChanged(value)
    }
  }
