package research.cut.draw

import com.ccfraser.muirwik.components.themeContext
import kotlinx.css.*
import kotlinx.css.Position
import kotlinx.html.classes
import kotlinx.html.js.*
import model.*
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.events.WheelEvent
import org.w3c.dom.get
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import root.debugLog
import styled.css
import styled.styledCanvas
import styled.styledDiv
import view.DrawView
import kotlin.browser.document
import kotlin.math.*

class DrawComponent(prps: DrawProps) : RComponent<DrawProps, DrawState>(prps) {

  private var resultWidth: Int = 0
  private var resultHeight: Int = 0
  private var verticalRatio: Double = 0.0
  private var horizontalRatio: Double = 0.0
  private var radiusRatio: Double = 0.0
  private var startX: Double = 0.0
  private var startY: Double = 0.0

  override fun componentDidMount() {
    updateCanvas()
  }

  override fun componentDidUpdate(prevProps: DrawProps, prevState: DrawState, snapshot: Any) {
    updateCanvas()
  }

  private fun updateCanvas() {
    val shape = props.drawModel.shape
    if (shape != null) {
      draw(shape)
    } else {
      clearCanvas()
    }
  }

  override fun RBuilder.render() {
    themeContext.Consumer { theme ->
      val dicomWidth = props.cut.data.screen_size_h
      val dicomHeight = props.cut.data.screen_size_v
      val ri = dicomWidth.toDouble() / dicomHeight
      val rs = props.width.toDouble() / props.height
      if (rs > ri) {
        resultWidth = dicomWidth * props.height / dicomHeight
        resultHeight = props.height
      } else {
        resultWidth = props.width
        resultHeight = dicomHeight * props.width / dicomWidth
      }
      val mTop = props.height - resultHeight
      val mLeft = props.width - resultWidth
      val resultTop = if (mTop <= 0) 0 else mTop / 2
      val resultLeft = if (mLeft <= 0) 0 else mLeft / 2
      verticalRatio = dicomHeight.toDouble() / resultHeight
      horizontalRatio = dicomWidth.toDouble() / resultWidth
      val dicomRadius = sqrt(dicomHeight.toDouble().pow(2) + dicomWidth.toDouble().pow(2))
      val screenRadius = sqrt(resultHeight.toDouble().pow(2) + resultWidth.toDouble().pow(2))
      radiusRatio = dicomRadius / screenRadius
      styledDiv {
        css {
          position = Position.absolute
          top = resultTop.px
          left = resultLeft.px
          width = resultWidth.px
          height = resultHeight.px
          zIndex = 3
        }
        styledCanvas {
          attrs {
            classes += "draw_canvas_${props.cut.type.intType}"
            width = resultWidth.toString()
            height = resultHeight.toString()
            onMouseDownFunction = { event ->
              val mouseEvent = event.asDynamic().nativeEvent as MouseEvent
              val rect = (mouseEvent.target as HTMLCanvasElement).getBoundingClientRect()
              startX = mouseEvent.clientX - rect.left
              startY = mouseEvent.clientY - rect.top
              props.eventOutput(
                DrawView.Event.MouseDown(
                  x = (mouseEvent.clientX - rect.left) * horizontalRatio,
                  y = (mouseEvent.clientY - rect.top) * verticalRatio,
                  metaKey = mouseEvent.ctrlKey || mouseEvent.metaKey,
                  button = mouseEvent.button,
                  shiftKey = mouseEvent.shiftKey,
                  altKey = mouseEvent.altKey
                )
              )
            }

            onMouseMoveFunction = { event ->
              val mouseEvent = event.asDynamic().nativeEvent as MouseEvent
              val rect = (mouseEvent.target as HTMLCanvasElement).getBoundingClientRect()
              props.eventOutput(
                DrawView.Event.MouseMove(
                  x = (mouseEvent.clientX - rect.left) * horizontalRatio,
                  y = (mouseEvent.clientY - rect.top) * verticalRatio
                )
              )
            }

            onMouseUpFunction = {
              val mouseEvent = castEvent(it)
              val rect = (mouseEvent.target as HTMLCanvasElement).getBoundingClientRect()
              startX = 0.0
              startY = 0.0
              props.eventOutput(
                DrawView.Event.MouseUp(
                  x = (mouseEvent.clientX - rect.left) * horizontalRatio,
                  y = (mouseEvent.clientY - rect.top) * verticalRatio
                )
              )
            }

            onMouseOutFunction = {
              props.eventOutput(DrawView.Event.MouseOut)
              startX = 0.0
              startY = 0.0
            }

            onWheelFunction = {
              val wheelEvent = it.asDynamic().nativeEvent as WheelEvent
              props.eventOutput(DrawView.Event.MouseWheel(deltaY = if (wheelEvent.deltaY < 0.0) -1 else 1))
            }

            onDoubleClickFunction = {
              props.eventOutput(DrawView.Event.DoubleClick)
            }
          }
        }
      }
    }
  }

  private fun draw(shape: Shape) {
    debugLog("MY: shape = $shape, shape is rect = ${shape is Rectangle}")
    when (shape) {
      is Circle -> drawCircle(shape)
      is Rectangle -> drawRectangle(shape)
      else -> throw NotImplementedError("private fun draw(shape: Shape?) shape not implemented")
    }
  }

  private fun drawCircle(circle: Circle) {
    circle.let {
      val canvas = document.getElementsByClassName("draw_canvas_${props.cut.type.intType}")[0] as HTMLCanvasElement
      val context = canvas.getContext("2d") as CanvasRenderingContext2D
      context.clearRect(
        0.0,
        0.0,
        canvas.width.toDouble(),
        canvas.height.toDouble()
      )

      context.beginPath()
      if (props.cut.isPlanar()) {
        val radiusX = circle.dicomRadiusHorizontal / horizontalRatio * 0.5
        val radiusY = circle.dicomRadiusVertical / verticalRatio * 0.5
        val centerX = startX + radiusX
        val centerY = startY + radiusY
        val step = 0.01
        var a = step
        val pi2 = PI * 2 - step

        context.strokeStyle = props.cut.color
        context.moveTo(
          centerX + radiusX * cos(0.0),
          centerY + radiusY * sin(0.0)
        )
        while (a < pi2) {
          context.lineTo(
            centerX + radiusX * cos(a),
            centerY + radiusY * sin(a)
          )
          a += step
        }

      } else {
        context.strokeStyle = "#00ff00"
        context.arc(
          circle.dicomCenterX / horizontalRatio,
          circle.dicomCenterY / verticalRatio,
          circle.dicomRadiusHorizontal / radiusRatio,
          0.0,
          2 * PI,
          false
        )
      }
      context.stroke()
      context.closePath()
    }
  }

  private fun drawRectangle(rectangle: Rectangle) {
    rectangle.let {
      val canvas = document.getElementsByClassName("draw_canvas_${props.cut.type.intType}")[0] as HTMLCanvasElement
      val context = canvas.getContext("2d") as CanvasRenderingContext2D
      context.clearRect(
        0.0,
        0.0,
        canvas.width.toDouble(),
        canvas.height.toDouble()
      )

      context.beginPath()
      context.strokeStyle = "#00ff00"
      val x = rectangle.dicomCenterX
      val y = rectangle.dicomCenterY
      val w = rectangle.dicomRadiusHorizontal
      val h = rectangle.dicomRadiusVertical
      context.rect(x, y, w, h)
      context.stroke()
      context.closePath()
    }
  }


  private fun clearCanvas() {
    val canvas = document.getElementsByClassName("draw_canvas_${props.cut.type.intType}")[0] as HTMLCanvasElement
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.clearRect(
      0.0,
      0.0,
      canvas.width.toDouble(),
      canvas.height.toDouble()
    )
  }

  private fun castEvent(it: Event): MouseEvent = it.asDynamic().nativeEvent as MouseEvent

}

class DrawState : RState

interface DrawProps : RProps {
  var cut: Cut
  var width: Int
  var height: Int
  var drawModel: DrawView.Model
  var eventOutput: (DrawView.Event) -> Unit
}

fun RBuilder.drawView(
  cut: Cut,
  width: Int,
  height: Int,
  drawModel: DrawView.Model,
  eventOutput: (DrawView.Event) -> Unit
) = child(DrawComponent::class) {
  attrs.cut = cut
  attrs.width = width
  attrs.height = height
  attrs.drawModel = drawModel
  attrs.eventOutput = eventOutput
}
