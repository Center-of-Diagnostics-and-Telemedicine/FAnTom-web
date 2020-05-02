package presentation.screen.research.cut

import Constants
import client.newmvi.draw.binder.DrawBinder
import client.newmvi.draw.view.DrawView
import com.badoo.reaktive.subject.publish.PublishSubject
import kotlinx.css.*
import kotlinx.html.classes
import kotlinx.html.js.*
import model.Circle
import model.SliceSizeData
import org.w3c.dom.*
import org.w3c.dom.events.*
import presentation.di.injectDrawCanvas
import react.*
import styled.*
import kotlin.browser.document
import kotlin.math.PI

class MviDrawCanvasComponent(props: MviDrawCanvasProps) :
  RComponent<MviDrawCanvasProps, MviDrawCanvasState>(props),
  DrawView {

  override val events: PublishSubject<DrawView.Event> = PublishSubject()
  private lateinit var binder: DrawBinder

  override fun MviDrawCanvasState.init(props: MviDrawCanvasProps) {
    binder = injectDrawCanvas(props.cutType)
  }

  override fun componentDidMount() {
    binder.detachView()
    binder.onStop()
    binder.attachView(this)
    binder.onStart()
  }

  override fun componentDidUpdate(
    prevProps: MviDrawCanvasProps,
    prevState: MviDrawCanvasState,
    snapshot: Any
  ) {
    if (prevProps.cutType != props.cutType) {
      binder.detachView()
      binder.onStop()
      binder = injectDrawCanvas(props.cutType)
      binder.attachView(this)
      binder.onStart()
    }
  }

  override fun show(model: DrawView.DrawViewModel) {
    if (model.circle != null) {
      draw(model.circle)
    } else {
      clearCanvas()
    }
  }

  override fun RBuilder.render() {
    val ri = 512.0 / props.sliceSizesData.height
    val rs = props.width.toDouble() / props.height
    val resultWidth: Int
    val resultHeight: Int
    if (rs > ri) {
      resultWidth = 512 * props.height / props.sliceSizesData.height
      resultHeight = props.height
    } else {
      resultWidth = props.width
      resultHeight = props.sliceSizesData.height * props.width / 512
    }
    val resultTop = (props.height - resultHeight) / 2
    val resultLeft = (props.width - resultWidth) / 2
    styledDiv {
      css {
        position = Position.absolute
        top = resultTop.px
        left = resultLeft.px
        zIndex = 3
      }
      styledCanvas {
        css {

        }
        attrs {
          classes += "shape_canvas shape_canvas_${props.cutType}"
          width = resultWidth.toString()
          height = resultHeight.toString()
          onMouseDownFunction = { event ->
            val mouseEvent = event.asDynamic().nativeEvent as MouseEvent
            val rect = (mouseEvent.target as HTMLCanvasElement).getBoundingClientRect()
            dispatch(
              DrawView.Event.MouseDown(
                x = mouseEvent.clientX - rect.left,
                y = mouseEvent.clientY - rect.top,
                metaKey = mouseEvent.ctrlKey || mouseEvent.metaKey,
                button = mouseEvent.button,
                shiftKey = mouseEvent.shiftKey
              )
            )
          }
          onMouseMoveFunction = { event ->
            val mouseEvent = event.asDynamic().nativeEvent as MouseEvent
            val rect = (mouseEvent.target as HTMLCanvasElement).getBoundingClientRect()
            dispatch(
              DrawView.Event.MouseMove(
                x = mouseEvent.clientX - rect.left,
                y = mouseEvent.clientY - rect.top
              )
            )
          }
          onMouseUpFunction = {
            val mouseEvent = castEvent(it)
            val rect = (mouseEvent.target as HTMLCanvasElement).getBoundingClientRect()
            dispatch(
              DrawView.Event.MouseUp(
                x = mouseEvent.clientX - rect.left,
                y = mouseEvent.clientY - rect.top
              )
            )
          }
          onMouseOutFunction = {
            dispatch(DrawView.Event.MouseOut)
          }

          onClickFunction = {
            val mouseEvent = castEvent(it)
            val rect = (mouseEvent.target as HTMLCanvasElement).getBoundingClientRect()
            dispatch(
              DrawView.Event.MouseClick(
                x = mouseEvent.clientX - rect.left,
                y = mouseEvent.clientY - rect.top,
                altKey = mouseEvent.altKey,
                metaKey = mouseEvent.ctrlKey || mouseEvent.metaKey
              )
            )
          }
          onWheelFunction = {
            val wheelEvent = it.asDynamic().nativeEvent as WheelEvent
            dispatch(DrawView.Event.MouseWheel(wheelEvent.deltaY))
          }
        }
      }
    }
  }


  private fun draw(circle: Circle?) {
    circle?.let {
      val canvas = document.getElementsByClassName("shape_canvas_${props.cutType}")[0] as HTMLCanvasElement
      val context = canvas.getContext("2d") as CanvasRenderingContext2D
      context.clearRect(
        Constants.ZERO,
        Constants.ZERO,
        canvas.width.toDouble(),
        canvas.height.toDouble()
      )
      context.strokeStyle = "#00ff00"
      context.beginPath()
      context.arc(
        circle.centerX,
        circle.centerY,
        circle.radius,
        0.0,
        2 * PI,
        false
      )
      context.stroke()
      context.closePath()
    }
  }

  private fun castEvent(it: Event): MouseEvent {
    return it.asDynamic().nativeEvent as MouseEvent
  }

  private fun clearCanvas() {
    val canvas = document.getElementsByClassName("shape_canvas_${props.cutType}")[0] as HTMLCanvasElement
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.clearRect(
      Constants.ZERO,
      Constants.ZERO,
      canvas.width.toDouble(),
      canvas.height.toDouble()
    )
  }

  override fun componentWillUnmount() {
    binder.detachView()
    binder.onStop()
  }

  override fun dispatch(event: DrawView.Event) {
    events.onNext(event)
  }
}

interface MviDrawCanvasProps : RProps {
  var cutType: Int
  var height: Int
  var width: Int
  var sliceSizesData: SliceSizeData
}

interface MviDrawCanvasState : RState {
  var circle: Circle?
}

fun RBuilder.mviDrawCanvas(
  DrawCanvasType: Int,
  height: Int,
  width: Int,
  sliceSizesData: SliceSizeData
) = child(MviDrawCanvasComponent::class) {
  attrs.cutType = DrawCanvasType
  attrs.height = height
  attrs.width = width
  attrs.sliceSizesData = sliceSizesData
}
