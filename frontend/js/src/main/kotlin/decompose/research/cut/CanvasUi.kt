package decompose.research.cut

import decompose.Props
import decompose.RenderableComponent
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.Element
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.get
import react.RState

abstract class CanvasUi<T : Any, S : RState, Model : Any>(
  props: Props<T>,
  initialState: S
) : RenderableComponent<T, S>(props = props, initialState = initialState) {

  protected var testRef: Element? = null

  override fun componentDidMount() {
    super.componentDidMount()
    window.addEventListener(type = "resize", callback = { compDidUpdate() })
  }

  protected open fun updateCanvas(model: Model) {
    clearCanvas()
  }

  protected fun clearCanvas() {
    val canvas = getCanvas()
    val context = canvas?.getContext()
    context?.clearRect(
      x = 0.0,
      y = 0.0,
      w = canvas.width.toDouble(),
      h = canvas.height.toDouble()
    )
  }

  abstract fun getCanvasName(): String

  protected fun getCanvas(): HTMLCanvasElement? =
    document.getElementsByClassName(getCanvasName())[0] as? HTMLCanvasElement

  protected fun HTMLCanvasElement.getContext(): CanvasRenderingContext2D? =
    getContext("2d") as? CanvasRenderingContext2D

  abstract fun getCanvasZIndex(): Int

  override fun componentWillUnmount() {
    super.componentWillUnmount()
    window.removeEventListener(type = "resize", {})
  }
}