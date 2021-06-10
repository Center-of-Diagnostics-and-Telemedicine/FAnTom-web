package decompose.research.cuts

import com.arkivanov.decompose.RouterState
import components.twoverticalcutscontainer.TwoVerticalCutsContainer
import components.twoverticalcutscontainer.TwoVerticalCutsContainer.Child
import decompose.RenderableComponent
import decompose.renderableChild
import decompose.research.cut.CutContainerUi
import decompose.research.cuts.TwoVerticalCutsContainerUi.State
import decompose.research.cuts.TwoVerticalCutsContainerUi.TwoVerticalCutsContainerStyles.columnOfRowsStyle
import kotlinx.css.*
import react.RBuilder
import react.RState
import styled.StyleSheet
import styled.css
import styled.styledDiv

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
    styledDiv {
      css(columnOfRowsStyle)
      renderableChild(CutContainerUi::class, state.topRouterState.activeChild.instance.component)
      renderableChild(CutContainerUi::class, state.bottomRouterState.activeChild.instance.component)
    }
  }

  object TwoVerticalCutsContainerStyles :
    StyleSheet("TwoVerticalCutsContainerStyles", isStatic = true) {
    val columnOfRowsStyle by css {
      flex(1.0)
      display = Display.flex
      flexDirection = FlexDirection.column
    }
  }

  class State(
    var topRouterState: RouterState<*, Child>,
    var bottomRouterState: RouterState<*, Child>,
  ) : RState
}