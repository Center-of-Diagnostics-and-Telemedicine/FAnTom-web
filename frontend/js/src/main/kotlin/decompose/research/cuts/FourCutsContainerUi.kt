package decompose.research.cuts

import com.arkivanov.decompose.RouterState
import components.fourcutscontainer.FourCutsContainer
import components.fourcutscontainer.FourCutsContainer.Child
import decompose.RenderableComponent
import decompose.renderableChild
import decompose.research.cut.CutContainerUi
import decompose.research.cuts.FourCutsContainerUi.FourCutsContainerStyles.columnOfRowsStyle
import decompose.research.cuts.FourCutsContainerUi.FourCutsContainerStyles.rowOfColumnsStyle
import decompose.research.cuts.FourCutsContainerUi.State
import kotlinx.css.*
import react.RBuilder
import react.RState
import styled.StyleSheet
import styled.css
import styled.styledDiv

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
    styledDiv {
      css(columnOfRowsStyle)

      styledDiv {
        css(rowOfColumnsStyle)
        renderableChild(CutContainerUi::class, state.topLeftRouterState.activeChild.instance.component)
        renderableChild(CutContainerUi::class, state.topRightRouterState.activeChild.instance.component)
      }

      styledDiv {
        css(rowOfColumnsStyle)
        renderableChild(CutContainerUi::class, state.bottomLeftRouterState.activeChild.instance.component)
        renderableChild(CutContainerUi::class, state.bottomRightRouterState.activeChild.instance.component)
      }
    }

  }

  object FourCutsContainerStyles : StyleSheet("FourCutsContainerStyles", isStatic = true) {

    val columnOfRowsStyle by css {
      flex(1.0)
      display = Display.flex
      flexDirection = FlexDirection.column
    }

    val rowOfColumnsStyle by css {
      flex(1.0)
      display = Display.flex
      flexDirection = FlexDirection.row
    }
  }

  class State(
    var topLeftRouterState: RouterState<*, Child>,
    var topRightRouterState: RouterState<*, Child>,
    var bottomLeftRouterState: RouterState<*, Child>,
    var bottomRightRouterState: RouterState<*, Child>,
  ) : RState
}