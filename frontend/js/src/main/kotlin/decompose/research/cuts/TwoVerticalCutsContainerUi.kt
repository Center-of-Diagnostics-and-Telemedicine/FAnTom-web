package decompose.research.cuts

import com.arkivanov.decompose.RouterState
import com.ccfraser.muirwik.components.mTypography
import components.twoverticalcutscontainer.TwoVerticalCutsContainer
import components.twoverticalcutscontainer.TwoVerticalCutsContainer.Child
import decompose.RenderableComponent
import decompose.research.cuts.TwoVerticalCutsContainerUi.State
import react.RBuilder
import react.RState

class TwoVerticalCutsContainerUi(props: Props<TwoVerticalCutsContainer>) :
  RenderableComponent<TwoVerticalCutsContainer, State>(
    props = props,
    initialState = State(
//    model = props.component.model.value,
      topRouterState = props.component.topRouterState.value,
      bottomRouterState = props.component.bottomRouterState.value,
    )
  ) {

  init {
//    component.model.bindToState { model = it }
    component.topRouterState.bindToState { topRouterState = it }
    component.bottomRouterState.bindToState { bottomRouterState = it }
  }

  override fun RBuilder.render() {
    mTypography { +"TwoVerticalCutsContainerUi" }
  }

  class State(
    var topRouterState: RouterState<*, Child>,
    var bottomRouterState: RouterState<*, Child>,
  ) : RState
}