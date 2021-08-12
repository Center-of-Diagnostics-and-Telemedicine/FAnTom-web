package decompose.research.cut

import com.arkivanov.decompose.RouterState
import components.cutcontainer.CutContainer
import decompose.Props
import decompose.RenderableComponent
import decompose.renderableChild
import decompose.research.cut.CutContainerUi.CutContainerStyles.cutContainerStyle
import decompose.research.cut.CutContainerUi.State
import decompose.research.cut.shapes.ShapesUi
import kotlinx.browser.window
import kotlinx.css.*
import org.w3c.dom.Element
import react.RBuilder
import react.RState
import react.dom.findDOMNode
import styled.StyleSheet
import styled.css
import styled.styledDiv

class CutContainerUi(props: Props<CutContainer>) :
  RenderableComponent<CutContainer, State>(
    props = props,
    initialState = State(
//      model = props.component.model.value,
      sliderRouterState = props.component.sliderRouterState.value,
      drawRouterState = props.component.drawRouterState.value,
      shapesRouterState = props.component.shapesRouterState.value,
      cutRouterState = props.component.cutRouterState.value,

      )
  ) {

  protected var testRef: Element? = null

  init {
//    component.model.bindToState { model = it }
    component.sliderRouterState.bindToState { sliderRouterState = it }
    component.drawRouterState.bindToState { drawRouterState = it }
    component.shapesRouterState.bindToState { shapesRouterState = it }
    component.cutRouterState.bindToState { cutRouterState = it }
  }

  override fun componentDidMount() {
    super.componentDidMount()
    window.addEventListener(type = "resize", callback = { compDidUpdate() })
    updateDimensions()
  }

  override fun compDidUpdate() = updateDimensions()

  private fun updateDimensions() =
    component.onScreenDimensionChanged(testRef?.clientHeight, testRef?.clientWidth)

  override fun RBuilder.render() {
    styledDiv {
      css(cutContainerStyle)
      styledDiv {
        css {
          grow(Grow.GROW_SHRINK)
        }
        ref {
          testRef = findDOMNode(it)
        }
        when (val instance = state.cutRouterState.activeChild.instance) {
          is CutContainer.CutChild.Data -> renderableChild(CutUi::class, instance.component)
          CutContainer.CutChild.None -> {
          }
        }
        when (val instance = state.shapesRouterState.activeChild.instance) {
          is CutContainer.ShapesChild.Data -> renderableChild(ShapesUi::class, instance.component)
          CutContainer.ShapesChild.None -> {
          }
        }
        when (val instance = state.drawRouterState.activeChild.instance) {
          is CutContainer.DrawChild.Data -> renderableChild(DrawUi::class, instance.component)
          CutContainer.DrawChild.None -> {
          }
        }
      }
      styledDiv {
        when (val instance = state.sliderRouterState.activeChild.instance) {
          is CutContainer.SliderChild.Data -> renderableChild(SliderUi::class, instance.component)
          CutContainer.SliderChild.None -> {
          }
        }
      }
    }
  }

  object CutContainerStyles : StyleSheet("CutContainerStyles", isStatic = true) {
    val cutContainerStyle by css {
      display = Display.flex
      flex(1.0, 1.0, FlexBasis.auto)
      flexDirection = FlexDirection.column
      position = Position.relative
      background = "#000"
    }
  }

  class State(
//    val model: Model,
    var sliderRouterState: RouterState<*, CutContainer.SliderChild>,
    var drawRouterState: RouterState<*, CutContainer.DrawChild>,
    var shapesRouterState: RouterState<*, CutContainer.ShapesChild>,
    var cutRouterState: RouterState<*, CutContainer.CutChild>,
  ) : RState
}