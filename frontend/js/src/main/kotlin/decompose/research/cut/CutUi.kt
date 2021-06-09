package decompose.research.cut

import com.arkivanov.decompose.RouterState
import com.ccfraser.muirwik.components.mTypography
import components.cut.Cut
import components.cut.Cut.Model
import decompose.RenderableComponent
import decompose.research.cut.CutUi.State
import kotlinx.css.*
import react.RBuilder
import react.RState
import styled.StyleSheet
import styled.css
import styled.styledDiv

class CutUi(props: Props<Cut>) :
  RenderableComponent<Cut, State>(
    props = props,
    initialState = State(
      model = props.component.model.value,
      sliderRouterState = props.component.sliderRouterState.value,
      drawRouterState = props.component.drawRouterState.value,
      shapesRouterState = props.component.shapesRouterState.value,

      )
  ) {

  init {
//    component.model.bindToState { model = it }
    component.sliderRouterState.bindToState { sliderRouterState = it }
    component.drawRouterState.bindToState { drawRouterState = it }
    component.shapesRouterState.bindToState { shapesRouterState = it }
  }

  override fun RBuilder.render() {
    mTypography { +"cut + ${state.model}" }
  }

  private fun RBuilder.emptyContainer() {
    styledDiv {
      css {
        display = Display.flex
        flex(1.0, 1.0, FlexBasis.auto)
      }
    }
  }

  object CutStyles : StyleSheet("CutStyles", isStatic = true) {

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
    val model: Model,
    var sliderRouterState: RouterState<*, Cut.SliderChild>,
    var drawRouterState: RouterState<*, Cut.DrawChild>,
    var shapesRouterState: RouterState<*, Cut.ShapesChild>,
  ) : RState
}