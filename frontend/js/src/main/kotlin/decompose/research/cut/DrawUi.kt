package decompose.research.cut

import components.draw.Draw
import components.draw.Draw.Model
import decompose.Props
import decompose.RenderableComponent
import decompose.research.cut.DrawUi.State
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.css.*
import kotlinx.html.classes
import kotlinx.html.js.*
import model.Circle
import model.Rectangle
import model.Shape
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.Element
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.WheelEvent
import org.w3c.dom.get
import react.RBuilder
import react.RErrorInfo
import react.RState
import react.dom.attrs
import react.dom.findDOMNode
import styled.css
import styled.styledCanvas
import styled.styledDiv

external interface DrawProps : Props<Draw> {

}

class DrawUi(props: DrawProps) : RenderableComponent<Draw, State>(
  props = props,
  initialState = State(model = props?.component?.model?.value)
) {

  private var testRef: Element? = null

  init {
    component.model.bindToState { model = it }
//    component.model.subscribe { updateCanvas(it) }
  }

  override fun componentDidMount() {
    super.componentDidMount()
    window.addEventListener("resize", { updateDimensions() })
    updateDimensions()
  }

  override fun compDidUpdate() {
    updateDimensions()
  }

  private fun updateDimensions() =
    component.onScreenDimensionChanged(testRef?.clientHeight, testRef?.clientWidth)

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
      }
      styledCanvas {
        this@styledCanvas.attrs {
          classes = classes + getCanvasName()
          width = state.model.screenDimensionsModel.calculatedScreenWidth.toString()
          height = state.model.screenDimensionsModel.calculatedScreenHeight.toString()
          onMouseDownFunction = onMouseDown()
          onMouseMoveFunction = onMouseMove()
          onMouseUpFunction = onMouseUp()
          onMouseOutFunction = onMouseOut()
          onWheelFunction = onMouseWheel()
          onDoubleClickFunction = onDoubleClick()
        }
      }
    }
  }

  private fun onMouseDown(): (Event) -> Unit = {
//    val mouseEvent = it.asDynamic().nativeEvent as MouseEvent
//    val rect = (mouseEvent.target as HTMLCanvasElement).getBoundingClientRect()
////    val mouseDownModel = MouseDown(
////      dicomX = mapScreenXToDicomX(
////        screenX = (mouseEvent.clientX - rect.left),
////        horizontalRatio = dimensions.horizontalRatio
////      ),
////      dicomY = mapScreenYToDicomY(
////        screenY = (mouseEvent.clientY - rect.top),
////        verticalRatio = dimensions.verticalRatio
////      ),
////      metaKey = mouseEvent.ctrlKey || mouseEvent.metaKey,
////      button = mouseEvent.button,
////      shiftKey = mouseEvent.shiftKey,
////      altKey = mouseEvent.altKey
////    )
//    component.onMouseDown(mouseDownModel)
  }

  private fun onMouseMove(): (Event) -> Unit = {
//    val mouseEvent = it.asDynamic().nativeEvent as MouseEvent
//    val rect = (mouseEvent.target as HTMLCanvasElement).getBoundingClientRect()
//    component.onMouseMove(
//      dicomX = mapScreenXToDicomX(
//        screenX = (mouseEvent.clientX - rect.left),
//        horizontalRatio = dimensions.horizontalRatio
//      ),
//      dicomY = mapScreenYToDicomY(
//        screenY = (mouseEvent.clientY - rect.top),
//        verticalRatio = dimensions.verticalRatio
//      ),
//    )
  }

  private fun onMouseWheel(): (Event) -> Unit = {
    val wheelEvent = it.asDynamic().nativeEvent as WheelEvent
    component.onMouseWheel(dicomDeltaY = if (wheelEvent.deltaY < 0.0) -1 else 1)
  }

  private fun onMouseUp(): (Event) -> Unit = { component.onMouseUp() }
  private fun onMouseOut(): (Event) -> Unit = { component.onMouseOut() }
  private fun onDoubleClick(): (Event) -> Unit = { component.onDoubleClick() }

  fun updateCanvas(model: Model) {
//    super.updateCanvas(model)
//    val canvas = getCanvas()
//    canvas?.let {
//      dimensions = model.plane.calculateScreenDimensions(
//        screenHeight = it.height,
//        screenWidth = it.width
//      )
//    }
//    model.shape?.let { draw(it) }
  }

  private fun clearCanvas() {
    val canvas = getCanvas()
    val context = canvas?.getContext()
    context?.clearRect(
      x = 0.0,
      y = 0.0,
      w = canvas.width.toDouble(),
      h = canvas.height.toDouble()
    )
  }


  private fun getCanvas(): HTMLCanvasElement? =
    document.getElementsByClassName(getCanvasName())[0] as? HTMLCanvasElement

  private fun HTMLCanvasElement.getContext(): CanvasRenderingContext2D? =
    getContext("2d") as? CanvasRenderingContext2D

  private fun draw(shape: Shape) {
    when (shape) {
      is Circle -> drawCircle(shape)
      is Rectangle -> drawRectangle(shape)
      else -> throw NotImplementedError("private fun draw(shape: Shape?) shape not implemented")
    }
  }

  private fun drawCircle(circle: Circle) {
//    val canvas = getCanvas()
//    val context = canvas?.getContext()
//
//    context?.beginPath()
//    context?.strokeStyle = "#00ff00"
//    val x = mapDicomXToScreenX(
//      dicomX = circle.dicomCenterX,
//      horizontalRatio = dimensions.horizontalRatio
//    ).plus(dimensions.left)
//    val y = mapDicomYToScreenY(
//      dicomY = circle.dicomCenterY,
//      verticalRatio = dimensions.verticalRatio
//    ).plus(dimensions.top)
//    val radius = mapDicomRadiusToScreenRadius(
//      dicomRadius = circle.dicomRadiusHorizontal,
//      screenRadius = dimensions.radiusRatio
//    )
//    context?.arc(
//      x = x,
//      y = y,
//      radius = radius,
//      startAngle = 0.0,
//      endAngle = 2 * PI,
//      anticlockwise = false
//    )
//    context?.stroke()
//    context?.closePath()
  }

  private fun drawRectangle(rectangle: Rectangle) {
//    debugLog("MY: ${rectangle}")
//    val canvas = getCanvas()
//    val context = canvas?.getContext()
//    context?.beginPath()
//    context?.strokeStyle = "#00ff00"
//    val x = mapDicomXToScreenX(
//      dicomX = rectangle.dicomCenterX,
//      horizontalRatio = dimensions.horizontalRatio
//    ).plus(dimensions.left)
//    val y = mapDicomYToScreenY(
//      dicomY = rectangle.dicomCenterY,
//      verticalRatio = dimensions.verticalRatio
//    ).plus(dimensions.top)
//    val w = mapDicomRadiusToScreenRadius(
//      dicomRadius = rectangle.dicomRadiusHorizontal,
//      screenRadius = dimensions.horizontalRatio
//    )
//    val h =
//      mapDicomRadiusToScreenRadius(
//        dicomRadius = rectangle.dicomRadiusVertical,
//        screenRadius = dimensions.verticalRatio
//      )
//    context?.rect(x, y, w, h)
//    context?.stroke()
//    context?.closePath()
  }

  private fun getCanvasName(): String = "draw_canvas_${state.model.cutType}"
  private fun getCanvasZIndex(): Int = 2

  override fun componentDidCatch(error: Throwable, info: RErrorInfo) {
    error.printStackTrace()
    println(info)
  }

  override fun componentWillUnmount() {
    super.componentWillUnmount()
    window.removeEventListener(type = "resize", {})
  }

  class State(var model: Model) : RState
}