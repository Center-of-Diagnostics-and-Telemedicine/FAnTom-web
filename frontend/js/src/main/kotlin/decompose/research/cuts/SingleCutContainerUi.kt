package decompose.research.cuts

import com.arkivanov.decompose.RouterState
import com.ccfraser.muirwik.components.mTypography
import components.singlecutcontainer.SingleCutContainer
import components.singlecutcontainer.SingleCutContainer.Child
import decompose.RenderableComponent
import decompose.research.cuts.SingleCutContainerUi.State
import react.RBuilder
import react.RState

class SingleCutContainerUi(props: Props<SingleCutContainer>) :
  RenderableComponent<SingleCutContainer, State>(
    props = props,
    initialState = State(
//    model = props.component.model.value,
      routerState = props.component.routerState.value
    )
  ) {

  init {
//    component.model.bindToState { model = it }
    component.routerState.bindToState { routerState = it }
  }

  override fun RBuilder.render() {
    mTypography { +"SingleCutContainerUi" }
  }

  class State(
    var routerState: RouterState<*, Child>,
  ) : RState
}