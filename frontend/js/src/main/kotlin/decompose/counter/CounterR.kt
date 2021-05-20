package decompose.counter

import com.ccfraser.muirwik.components.MPaperVariant
import com.ccfraser.muirwik.components.MTypographyAlign
import com.ccfraser.muirwik.components.mPaper
import com.ccfraser.muirwik.components.mTypography
import decompose.RenderableComponent
import decompose.counter.CounterR.State
import react.RBuilder
import react.RState

class CounterR(props: Props<Counter>) : RenderableComponent<Counter, State>(
  props = props,
  initialState = State(model = props.component.model.value)
) {

  init {
    component.model.bindToState { model = it }
  }

  override fun RBuilder.render() {
    mPaper(variant = MPaperVariant.outlined) {
      mTypography(state.model.text, align = MTypographyAlign.center)
    }
  }

  class State(
    var model: Counter.Model
  ) : RState
}