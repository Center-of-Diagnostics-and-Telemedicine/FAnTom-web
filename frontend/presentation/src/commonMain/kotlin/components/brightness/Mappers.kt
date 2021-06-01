package components.brightness

import components.brightness.Brightness.Model
import store.tools.BrightnessStore.State


internal val stateToModel: (State) -> Model =
  {
    Model(
      blackValue = it.blackValue,
      whiteValue = it.whiteValue,
      gammaValue = it.gammaValue
    )
  }