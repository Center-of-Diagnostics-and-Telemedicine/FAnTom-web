package decompose.research.cut

import com.ccfraser.muirwik.components.*
import components.cutslider.Slider
import decompose.RenderableComponent
import decompose.research.cut.SliderUi.State
import react.RBuilder
import react.RState
import decompose.Props

class SliderUi(props: Props<Slider>) : RenderableComponent<Slider, State>(
  props = props,
  initialState = State(model = props.component.model.value)
) {

  init {
    component.model.bindToState { model = it }
  }

  override fun RBuilder.render() {
    mSlider(
      value = state.model.currentValue,
      defaultValue = state.model.defaultValue,
      min = 1,
      max = state.model.maxValue,
      orientation = MSliderOrientation.horizontal,
      valueLabelDisplay = MSliderValueLabelDisplay.auto,
      onChange = { _, newValue ->
//        sliderViewDelegate.dispatch(SliderView.Event.HandleOnChange(newValue.toInt()))
      }
    )
  }

  class State(var model: Slider.Model) : RState
}