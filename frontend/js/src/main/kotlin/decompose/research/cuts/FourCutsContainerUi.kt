package decompose.research.cuts

import com.arkivanov.decompose.RouterState
import com.ccfraser.muirwik.components.mTypography
import components.fourcutscontainer.FourCutsContainer
import components.fourcutscontainer.FourCutsContainer.Child
import decompose.RenderableComponent
import decompose.research.cuts.FourCutsContainerUi.State
import react.RBuilder
import react.RState

class FourCutsContainerUi(props: Props<FourCutsContainer>) :
  RenderableComponent<FourCutsContainer, State>(
    props = props,
    initialState = State(
//    model = props.component.model.value,
      topLeftRouterState = props.component.topLeftRouterState.value,
      topRightRouterState = props.component.topRightRouterState.value,
      bottomLeftRouterState = props.component.bottomLeftRouterState.value,
      bottomRightRouterState = props.component.bottomRightRouterState.value,
    )
  ) {

  init {
//    component.model.bindToState { model = it }
    component.topLeftRouterState.bindToState { topLeftRouterState = it }
    component.topRightRouterState.bindToState { topRightRouterState = it }
    component.bottomLeftRouterState.bindToState { bottomLeftRouterState = it }
    component.bottomRightRouterState.bindToState { bottomRightRouterState = it }
  }

  override fun RBuilder.render() {
    mTypography { +"fourCutsContainer" }
  }

  class State(
    var topLeftRouterState: RouterState<*, Child>,
    var topRightRouterState: RouterState<*, Child>,
    var bottomLeftRouterState: RouterState<*, Child>,
    var bottomRightRouterState: RouterState<*, Child>,
  ) : RState
}