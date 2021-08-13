package decompose.research.cut

import components.models.shape.ScreenCircle
import components.models.shape.ScreenEllipse
import components.models.shape.ScreenRectangle
import components.models.shape.ScreenShape
import decompose.Props
import decompose.RenderableComponent
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.get
import react.RState
import kotlin.math.PI

abstract class CanvasUi<T : Any, S : RState, Model : Any>(
  props: Props<T>,
  initialState: S
) : RenderableComponent<T, S>(props = props, initialState = initialState) {

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

  protected fun drawShape(shape: ScreenShape) {
    when (shape) {
      is ScreenCircle -> drawCircle(shape)
      is ScreenEllipse -> drawEllipse(shape)
      is ScreenRectangle -> drawRectangle(shape)
      else -> throw NotImplementedError("private fun draw(shape: Shape?) shape not implemented")
    }
  }

  private fun drawCircle(circle: ScreenCircle) {
    val canvas = getCanvas()
    val context = canvas?.getContext()

    context?.beginPath()
    context?.strokeStyle = circle.color//"#00ff00"
    if (circle.highlight) {
      context?.lineWidth = 2.0
    } else {
      context?.lineWidth = 1.0
    }
    context?.arc(
      x = circle.screenX,
      y = circle.screenY,
      radius = circle.screenWidth / 2,
      startAngle = 0.0,
      endAngle = 2 * PI,
      anticlockwise = false
    )
    context?.stroke()
    context?.closePath()
  }

  private fun drawEllipse(ellipse: ScreenEllipse) {
    val radiusX = ellipse.screenWidth / 2
    val radiusY = ellipse.screenHeight / 2
    val centerX = ellipse.screenX + radiusX
    val centerY = ellipse.screenY + radiusY

    val context = getCanvas()?.getContext()
    context?.strokeStyle = ellipse.color//"#00ff00"
    if (ellipse.highlight) {
      context?.lineWidth = 2.0
    } else {
      context?.lineWidth = 1.0
    }

    context?.save()
    context?.beginPath()

    context?.translate(centerX - radiusX, centerY - radiusY)
    context?.scale(radiusX, radiusY)
    context?.arc(1.0, 1.0, 1.0, 0.0, 2 * PI, false)

    context?.restore()
    context?.stroke()
  }

  private fun drawRectangle(rectangle: ScreenRectangle) {
    val canvas = getCanvas()
    val context = canvas?.getContext()
    context?.beginPath()
    context?.strokeStyle = rectangle.color//"#00ff00"
    if (rectangle.highlight) {
      context?.lineWidth = 2.0
    } else {
      context?.lineWidth = 1.0
    }
    context?.rect(
      x = rectangle.screenX,
      y = rectangle.screenY,
      w = rectangle.screenWidth,
      h = rectangle.screenHeight
    )
    context?.stroke()
    context?.closePath()
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