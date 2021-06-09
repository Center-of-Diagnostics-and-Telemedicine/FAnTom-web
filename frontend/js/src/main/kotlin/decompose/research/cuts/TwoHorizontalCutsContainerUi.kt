package decompose.research.cuts

import com.arkivanov.decompose.RouterState
import com.ccfraser.muirwik.components.mTypography
import components.twohorizontalcutscontainer.TwoHorizontalCutsContainer
import components.twohorizontalcutscontainer.TwoHorizontalCutsContainer.Child
import decompose.RenderableComponent
import decompose.research.cuts.TwoHorizontalCutsContainerUi.State
import react.RBuilder
import react.RState

class TwoHorizontalCutsContainerUi(props: Props<TwoHorizontalCutsContainer>) :
  RenderableComponent<TwoHorizontalCutsContainer, State>(
    props = props,
    initialState = State(
//    model = props.component.model.value,
      leftRouterState = props.component.leftRouterState.value,
      rightRouterState = props.component.rightRouterState.value,
    )
  ) {

  init {
//    component.model.bindToState { model = it }
    component.leftRouterState.bindToState { leftRouterState = it }
    component.rightRouterState.bindToState { rightRouterState = it }
  }

  override fun RBuilder.render() {
    mTypography { +"TwoHorizontalCutsContainerUi" }
  }

  class State(
    var leftRouterState: RouterState<*, Child>,
    var rightRouterState: RouterState<*, Child>,
  ) : RState
}