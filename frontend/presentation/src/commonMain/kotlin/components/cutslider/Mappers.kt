package components.cutslider

import components.cutslider.Slider.Model
import store.slider.SliderStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      currentValue = it.currentValue,
      maxValue = it.maxValue,
      defaultValue = it.defaultValue,
    )
  }