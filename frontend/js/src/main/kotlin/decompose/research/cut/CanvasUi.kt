package decompose.research.cut

import decompose.Props
import decompose.RenderableComponent
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.classes
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.Element
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.get
import react.RBuilder
import react.RState
import react.dom.attrs
import react.dom.findDOMNode
import styled.css
import styled.styledCanvas
import styled.styledDiv

abstract class CanvasUi<T : Any, S : RState, Model: Any>(
  props: Props<T>,
  initialState: S
) : RenderableComponent<T, S>(props = props, initialState = initialState) {

  protected var testRef: Element? = null

  override fun componentDidMount() {
    window.addEventListener(type = "resize", callback = { callToRenderContent() })
  }

  override fun RBuilder.render() {
    styledDiv {
      css {
        position = Position.absolute
        top = 0.px
        left = 0.px
        zIndex = getCanvasZIndex()
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

  protected fun callToRenderContent() {
    GlobalScope.launch {
      testRef?.let {
        renderContent(it.clientHeight, it.clientWidth)
      }
    }
  }

  protected fun renderContent(clientHeight: Int, clientWidth: Int) {
    testRef?.let {
      react.dom.render(it) {
        styledCanvas {
          this@styledCanvas.attrs {
            classes = classes + getCanvasName()
            width = clientWidth.toString()
            height = clientHeight.toString()
          }
        }
      }
    }
  }

  protected open fun updateCanvas(model: Model) {
    clearCanvas()
  }

  protected fun clearCanvas() {
    val canvas = document.getElementsByClassName(getCanvasName())[0] as? HTMLCanvasElement
    val context = canvas?.getContext("2d") as? CanvasRenderingContext2D
    context?.clearRect(
      x = 0.0,
      y = 0.0,
      w = canvas.width.toDouble(),
      h = canvas.height.toDouble()
    )
  }

  abstract fun getCanvasName(): String

  abstract fun getCanvasZIndex(): Int

  override fun componentWillUnmount() {
    window.removeEventListener(type = "resize", {})
  }
}