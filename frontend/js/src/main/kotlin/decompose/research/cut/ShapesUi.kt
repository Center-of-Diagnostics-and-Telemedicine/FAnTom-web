package decompose.research.cut

import components.shapes.Shapes
import components.shapes.Shapes.Model
import decompose.Props
import decompose.RenderableComponent
import decompose.research.cut.ShapesUi.State
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.classes
import org.w3c.dom.Element
import react.RBuilder
import react.RState
import react.dom.attrs
import react.dom.findDOMNode
import styled.css
import styled.styledCanvas
import styled.styledDiv

class ShapesUi(props: Props<Shapes>) : RenderableComponent<Shapes, State>(
  props = props,
  initialState = State(model = props.component.model.value)
) {

  private var testRef: Element? = null

  init {
    component.model.bindToState { model = it }
  }

  override fun RBuilder.render() {
    styledDiv {
      css {
        position = Position.absolute
        top = 0.px
        left = 0.px
        zIndex = 1
        width = 100.pct
        height = 100.pct
      }
      ref {
        testRef = findDOMNode(it)
        if (testRef != null) {
          callToRenderContent()
        }
      }
    }
  }

  override fun componentDidMount() {
    window.addEventListener(type = "resize", callback = { callToRenderContent() })
  }

  private fun callToRenderContent() {
    GlobalScope.launch {
      testRef?.let {
        renderContent(it.clientHeight, it.clientWidth)
      }
    }
  }

  private fun renderContent(clientHeight: Int, clientWidth: Int) {
    testRef?.let {
      react.dom.render(it) {
        styledCanvas {
          this@styledCanvas.attrs {
            classes = classes + "shape_canvas_${state.model.cutType}"
            width = clientWidth.toString()
            height = clientHeight.toString()
          }
        }
      }
    }
  }

  override fun componentWillUnmount() {
    window.removeEventListener(type = "resize", {})
  }

  class State(var model: Model) : RState
}