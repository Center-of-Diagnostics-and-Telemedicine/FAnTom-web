package mapper

import controller.SliderController.Output
import store.slider.SliderStore.Intent
import store.slider.SliderStore.State
import view.SliderView.Event
import view.SliderView.Model

val sliderStateToSliderModel: State.() -> Model? = {
  Model(
    currentValue = currentValue,
    maxValue = maxValue,
    defaultValue = defaultValue
  )
}

val sliderEventToSlideIntent: Event.() -> Intent? =
  {
    when (this) {
      is Event.HandleOnChange -> Intent.HandleChange(value = value)
    }
  }

val sliderEventToOutput: Event.() -> Output? = {
  when (this) {
    is Event.HandleOnChange -> Output.SliceNumberChanged(sliceNumber = value)
  }
}
