package decompose.research.cut

import components.draw.Draw
import components.draw.Draw.Model
import decompose.Props
import decompose.research.cut.DrawUi.State
import kotlinx.css.*
import kotlinx.html.classes
import kotlinx.html.js.*
import model.MouseDown
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.events.WheelEvent
import react.RBuilder
import react.RErrorInfo
import react.RState
import react.dom.attrs
import styled.css
import styled.styledCanvas
import styled.styledDiv

class DrawUi(props: Props<Draw>) : CanvasUi<Draw, State, Model>(
  props = props,
  initialState = State(model = props.component.model.value)
) {

  init {
    component.model.bindToState {
      model = it
      updateCanvas(it)
    }
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
      styledCanvas {
        css {
          marginLeft = state.model.screenDimensionsModel.left.px
          marginTop = state.model.screenDimensionsModel.top.px
        }
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

  override fun updateCanvas(model: Model) {
    super.updateCanvas(model)
    model.shape?.let { shape -> drawShape(shape) }
  }

  private fun onMouseDown(): (Event) -> Unit = {
    val mouseEvent = it.asDynamic().nativeEvent as MouseEvent
    val rect = (mouseEvent.target as HTMLCanvasElement).getBoundingClientRect()
    val mouseDownModel = MouseDown(
      screenX = mouseEvent.clientX - rect.left,
      screenY = mouseEvent.clientY - rect.top,
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
      screenX = mouseEvent.clientX - rect.left,
      screenY = mouseEvent.clientY - rect.top,
    )
  }

  private fun onMouseWheel(): (Event) -> Unit = {
    val wheelEvent = it.asDynamic().nativeEvent as WheelEvent
    component.onMouseWheel(screenDeltaY = wheelEvent.deltaY)
  }

  private fun onMouseUp(): (Event) -> Unit = { component.onMouseUp() }
  private fun onMouseOut(): (Event) -> Unit = { component.onMouseOut() }
  private fun onDoubleClick(): (Event) -> Unit = { component.onDoubleClick() }

  override fun getCanvasName(): String = "draw_canvas_${state.model.cutType}"
  override fun getCanvasZIndex(): Int = 2

  override fun componentDidCatch(error: Throwable, info: RErrorInfo) {
    error.printStackTrace()
    println(info)
  }

  class State(var model: Model) : RState
}