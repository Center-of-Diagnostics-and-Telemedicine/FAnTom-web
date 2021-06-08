package decompose.research.cuts

import components.cutscontainer.CutsContainer
import components.cutscontainer.CutsContainer.Model
import decompose.RenderableComponent
import decompose.research.cuts.CutsContainerUi.State
import react.RBuilder
import react.RState

class CutsContainerUi(props: Props<CutsContainer>) : RenderableComponent<CutsContainer, State>(
  props = props,
  initialState = State(
    model = props.component.model.value,
    open = false
  )
) {

  init {
    component.model.bindToState { model = it }
  }

  override fun RBuilder.render() {

  }

  class State(
    var model: Model,
    var open: Boolean
  ) : RState
}