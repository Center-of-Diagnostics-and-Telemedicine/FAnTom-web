package decompose.research.cuts

import com.arkivanov.decompose.RouterState
import components.twohorizontalcutscontainer.TwoHorizontalCutsContainer
import components.twohorizontalcutscontainer.TwoHorizontalCutsContainer.Child
import decompose.RenderableComponent
import decompose.renderableChild
import decompose.research.cut.CutContainerUi
import decompose.research.cuts.TwoHorizontalCutsContainerUi.State
import decompose.research.cuts.TwoHorizontalCutsContainerUi.TwoHorizontalCutsContainerStyles.rowOfColumnsStyle
import kotlinx.css.*
import react.RBuilder
import react.RState
import styled.StyleSheet
import styled.css
import styled.styledDiv

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
    styledDiv {
      css(rowOfColumnsStyle)
      renderableChild(CutContainerUi::class, state.leftRouterState.activeChild.instance.component)
      renderableChild(CutContainerUi::class, state.rightRouterState.activeChild.instance.component)
    }
  }

  object TwoHorizontalCutsContainerStyles :
    StyleSheet("TwoHorizontalCutsContainerStyles", isStatic = true) {
    val rowOfColumnsStyle by css {
      flex(1.0)
      display = Display.flex
      flexDirection = FlexDirection.row
    }
  }

  class State(
    var leftRouterState: RouterState<*, Child>,
    var rightRouterState: RouterState<*, Child>,
  ) : RState
}