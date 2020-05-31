package mapper

import controller.SliderController.Output
import store.cut.SliderStore.Intent
import store.cut.SliderStore.State
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
