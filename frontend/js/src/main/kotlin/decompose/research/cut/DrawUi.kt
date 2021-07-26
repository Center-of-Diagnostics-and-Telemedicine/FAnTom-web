package decompose.research.cut

import com.badoo.reaktive.observable.Observable
import components.draw.Draw
import components.draw.Draw.Model
import controller.CutController
import decompose.RenderableComponent
import decompose.research.cut.DrawUi.State
import kotlinx.css.*
import kotlinx.html.classes
import kotlinx.html.js.*
import react.dom.attrs
import styled.css
import styled.styledCanvas
import styled.styledDiv
import decompose.Props
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Element
import react.*
import react.dom.findDOMNode
import research.cut.*

class DrawUi(props: Props<Draw>) : RenderableComponent<Draw, State>(
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
        zIndex = 2
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
            classes = classes + "draw_canvas_${state.model.cutType}"
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