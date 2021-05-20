package decompose.myroot

import com.arkivanov.decompose.RouterState
import com.ccfraser.muirwik.components.MPaperVariant
import com.ccfraser.muirwik.components.mPaper
import decompose.RenderableComponent
import decompose.list.ResearchListR
import decompose.login.LoginR
import decompose.myroot.MyRootR.State
import decompose.renderableChild
import react.RBuilder
import react.RState

class MyRootR(props: Props<MyRoot>) : RenderableComponent<MyRoot, State>(
  props = props,
  initialState = State(routerState = props.component.routerState.value)
) {

  init {
    component.routerState.bindToState { routerState = it }
  }

  override fun RBuilder.render() {
    mPaper(variant = MPaperVariant.outlined) {
      when (val child = state.routerState.activeChild.instance) {
        is MyRoot.Child.Login -> renderableChild(LoginR::class, child.component)
        is MyRoot.Child.List -> renderableChild(ResearchListR::class, child.component)
      }
    }
  }

  class State(
    var routerState: RouterState<*, MyRoot.Child>,
  ) : RState
}