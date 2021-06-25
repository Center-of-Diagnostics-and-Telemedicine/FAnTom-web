package decompose.research.cuts

import com.arkivanov.decompose.RouterState
import components.cutscontainer.CutsContainer
import components.cutscontainer.CutsContainer.Child
import components.cutscontainer.CutsContainer.Model
import decompose.RenderableComponent
import decompose.renderableChild
import decompose.research.cuts.CutsContainerUi.State
import react.RBuilder
import react.RState
import decompose.Props

class CutsContainerUi(props: Props<CutsContainer>) : RenderableComponent<CutsContainer, State>(
  props = props,
  initialState = State(
    model = props.component.model.value,
    routerState = props.component.routerState.value,
    open = false
  )
) {

  init {
    component.model.bindToState { model = it }
    component.routerState.bindToState { routerState = it }
  }

  override fun RBuilder.render() {
    when (val instance = state.routerState.activeChild.instance) {
      is Child.Single -> renderableChild(SingleCutContainerUi::class, instance.component)
      is Child.TwoVertical -> renderableChild(TwoVerticalCutsContainerUi::class, instance.component)
      is Child.TwoHorizontal ->
        renderableChild(TwoHorizontalCutsContainerUi::class, instance.component)
      is Child.Four -> renderableChild(FourCutsContainerUi::class, instance.component)
    }
  }

  class State(
    var model: Model,
    var routerState: RouterState<*, Child>,
    var open: Boolean,
  ) : RState
}