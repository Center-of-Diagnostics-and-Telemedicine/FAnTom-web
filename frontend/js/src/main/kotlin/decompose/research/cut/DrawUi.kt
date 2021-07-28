package decompose.research.cut

import components.draw.Draw
import components.draw.Draw.Model
import components.draw.calculateScreenDimensions
import decompose.Props
import decompose.research.cut.DrawUi.State
import kotlinx.browser.document
import kotlinx.css.*
import kotlinx.html.classes
import kotlinx.html.js.*
import model.*
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.events.WheelEvent
import org.w3c.dom.get
import react.*
import react.dom.attrs
import research.cut.*
import styled.styledCanvas
import kotlin.math.PI

class DrawUi(props: Props<Draw>) : CanvasUi<Draw, State, Model>(
  props = props,
  initialState = State(model = props.component.model.value)
) {

  private var dimensions: ScreenDimensionsModel = initialScreenDimensionsModel()

  init {
    component.model.subscribe { updateCanvas(it) }
  }

  override fun RBuilder.renderCanvas(clientWidth: Int, clientHeight: Int) {
    styledCanvas {
      this@styledCanvas.attrs {
        classes = classes + getCanvasName()
        width = clientWidth.toString()
        height = clientHeight.toString()
        onMouseDownFunction = onMouseDown()
        onMouseMoveFunction = onMouseMove()
        onMouseUpFunction = onMouseUp()
        onMouseOutFunction = onMouseOut()
        onWheelFunction = onMouseWheel()
        onDoubleClickFunction = onDoubleClick()
      }
    }
  }

  private fun onMouseDown(): (Event) -> Unit = {
    val mouseEvent = it.asDynamic().nativeEvent as MouseEvent
    val rect = (mouseEvent.target as HTMLCanvasElement).getBoundingClientRect()
    val mouseDownModel = MouseDown(
      dicomX = (mouseEvent.clientX - rect.left) * dimensions.horizontalRatio,
      dicomY = (mouseEvent.clientY - rect.top) * dimensions.verticalRatio,
      metaKey = mouseEvent.ctrlKey || mouseEvent.metaKey,
      button = mouseEvent.button,
      shiftKey = mouseEvent.shiftKey,
      altKey = mouseEvent.altKey
    )
    component.onMouseDown(mouseDownModel)
  }

  private fun onMouseMove(): (Event) -> Unit = {
    val mouseEvent = it.asDynamic().nativeEvent as MouseEvent
    val rect = (mouseEvent.target as HTMLCanvasElement).getBoundingClientRect()
    component.onMouseMove(
      dicomX = (mouseEvent.clientX - rect.left) * dimensions.horizontalRatio,
      dicomY = (mouseEvent.clientY - rect.top) * dimensions.verticalRatio
    )
  }

  private fun onMouseUp(): (Event) -> Unit = { component.onMouseUp() }
  private fun onMouseOut(): (Event) -> Unit = { component.onMouseOut() }

  private fun onMouseWheel(): (Event) -> Unit = {
    val wheelEvent = it.asDynamic().nativeEvent as WheelEvent
    component.onMouseWheel(dicomDeltaY = if (wheelEvent.deltaY < 0.0) -1 else 1)
  }

  private fun onDoubleClick(): (Event) -> Unit = { component.onDoubleClick() }

  override fun updateCanvas(model: Model) {
    super.updateCanvas(model)
    val canvas = getCanvas()
    canvas?.let {
      dimensions = model.plane.calculateScreenDimensions(
        screenHeight = it.height,
        screenWidth = it.width
      )
    }
    model.shape?.let { draw(it) }
  }

  private fun draw(shape: Shape) {
    when (shape) {
      is Circle -> drawCircle(shape)
      is Rectangle -> drawRectangle(shape)
      else -> throw NotImplementedError("private fun draw(shape: Shape?) shape not implemented")
    }
  }

  private fun drawCircle(circle: Circle) {
    val canvas = getCanvas()
    val context = canvas?.getContext()

    context?.beginPath()
    context?.strokeStyle = "#00ff00"
    context?.arc(
      circle.dicomCenterX / dimensions.horizontalRatio,
      circle.dicomCenterY / dimensions.verticalRatio,
      circle.dicomRadiusHorizontal / dimensions.radiusRatio,
      0.0,
      2 * PI,
      false
    )
    context?.stroke()
    context?.closePath()
  }

  private fun drawRectangle(rectangle: Rectangle) {
    val canvas = getCanvas()
    val context = canvas?.getContext()
    context?.beginPath()
    context?.strokeStyle = "#00ff00"
    val w = rectangle.dicomRadiusHorizontal / dimensions.horizontalRatio
    val h = rectangle.dicomRadiusVertical / dimensions.verticalRatio
    context?.rect(rectangle.dicomCenterX, rectangle.dicomCenterY, w, h)
    context?.stroke()
    context?.closePath()
  }

  override fun getCanvasName(): String = "draw_canvas_${state.model.cutType}"
  override fun getCanvasZIndex(): Int = 2

  class State(var model: Model) : RState
}