package decompose.research.cuts

import com.arkivanov.decompose.RouterState
import components.singlecutcontainer.SingleCutContainer
import components.singlecutcontainer.SingleCutContainer.Child
import decompose.RenderableComponent
import decompose.renderableChild
import decompose.research.cut.CutContainerUi
import decompose.research.cuts.SingleCutContainerUi.State
import react.RBuilder
import react.RState
import decompose.Props

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
    renderableChild(CutContainerUi::class, state.routerState.activeChild.instance.component)
  }

  class State(
    var routerState: RouterState<*, Child>,
  ) : RState
}