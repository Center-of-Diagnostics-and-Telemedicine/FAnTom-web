package research.cut.draw

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.ccfraser.muirwik.components.themeContext
import controller.DrawController
import controller.DrawControllerImpl
import destroy
import kotlinx.css.*
import kotlinx.html.classes
import kotlinx.html.js.*
import model.Circle
import model.Cut
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.events.WheelEvent
import org.w3c.dom.get
import react.*
import resume
import root.debugLog
import styled.css
import styled.styledCanvas
import styled.styledDiv
import view.DrawView
import view.initialDrawModel
import kotlin.browser.document
import kotlin.math.PI

class DrawComponent(prps: DrawProps) : RComponent<DrawProps, DrawState>(prps) {

  private val drawViewDelegate = DrawViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: DrawController
  private var resultWidth: Int = 0
  private var resultHeight: Int = 0
  private var horizontalRatio: Double = 0.0
  private var verticalRatio: Double = 0.0

  init {
    state = DrawState(initialDrawModel())
  }

  override fun componentDidMount() {
    lifecycleRegistry.resume()
    controller = createController()
    controller.onViewCreated(drawViewDelegate, lifecycleRegistry)
    updateCanvas()
  }

  override fun componentDidUpdate(prevProps: DrawProps, prevState: DrawState, snapshot: Any) {
    updateCanvas()
  }

  private fun createController(): DrawController {
    val dependencies = props.dependencies
    val cutControllerDependencies =
      object : DrawController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
      }
    return DrawControllerImpl(cutControllerDependencies)
  }

  private fun updateCanvas() {
    val circle = state.drawModel.circle
    if (circle != null) {
      draw(circle)
    } else {
      clearCanvas()
    }
  }

  override fun RBuilder.render() {
    themeContext.Consumer { theme ->
      val dicomWidth = props.dependencies.cut.verticalCutData.data.maxFramesSize
      val dicomHeight = props.dependencies.cut.data!!.height
      val ri = dicomWidth.toDouble() / dicomHeight
      val rs = props.dependencies.width.toDouble() / props.dependencies.height
      if (rs > ri) {
        debugLog("rs > ri")
        resultWidth = dicomWidth * props.dependencies.height / dicomHeight
        resultHeight = props.dependencies.height
      } else {
        debugLog("rs <= ri")
        resultWidth = props.dependencies.width
        resultHeight = dicomHeight * props.dependencies.width / dicomWidth
      }
      val mTop = props.dependencies.height - resultHeight
      val mLeft = props.dependencies.width - resultWidth
      val resultTop = if (mTop <= 0) 0 else mTop / 2
      val resultLeft = if (mLeft <= 0) 0 else mLeft / 2
      horizontalRatio = dicomHeight.toDouble() / resultHeight
      verticalRatio = dicomWidth.toDouble() / resultWidth
      styledDiv {
        css {
          position = Position.absolute
          top = resultTop.px
          left = resultLeft.px
          zIndex = 3
        }
        styledCanvas {
          attrs {
            classes += "draw_canvas_${props.dependencies.cut.type.intType}"
            width = resultWidth.toString()
            height = resultHeight.toString()
            onMouseDownFunction = { event ->
              val mouseEvent = event.asDynamic().nativeEvent as MouseEvent
              val rect = (mouseEvent.target as HTMLCanvasElement).getBoundingClientRect()
              drawViewDelegate.dispatch(
                DrawView.Event.MouseDown(
                  x = (mouseEvent.clientX - rect.left) * verticalRatio,
                  y = (mouseEvent.clientY - rect.top) * horizontalRatio,
                  metaKey = mouseEvent.ctrlKey || mouseEvent.metaKey,
                  button = mouseEvent.button,
                  shiftKey = mouseEvent.shiftKey
                )
              )
            }
            onMouseMoveFunction = { event ->
              val mouseEvent = event.asDynamic().nativeEvent as MouseEvent
              val rect = (mouseEvent.target as HTMLCanvasElement).getBoundingClientRect()
              drawViewDelegate.dispatch(
                DrawView.Event.MouseMove(
                  x = (mouseEvent.clientX - rect.left) * verticalRatio,
                  y = (mouseEvent.clientY - rect.top) * horizontalRatio
                )
              )
            }
            onMouseUpFunction = {
              val mouseEvent = castEvent(it)
              val rect = (mouseEvent.target as HTMLCanvasElement).getBoundingClientRect()
              drawViewDelegate.dispatch(
                DrawView.Event.MouseUp(
                  x = (mouseEvent.clientX - rect.left) * verticalRatio,
                  y = (mouseEvent.clientY - rect.top) * horizontalRatio
                )
              )
            }
            onMouseOutFunction = {
              drawViewDelegate.dispatch(DrawView.Event.MouseOut)
            }

            onClickFunction = {
              val mouseEvent = castEvent(it)
              val rect = (mouseEvent.target as HTMLCanvasElement).getBoundingClientRect()
              drawViewDelegate.dispatch(
                DrawView.Event.MouseClick(
                  x = (mouseEvent.clientX - rect.left) * verticalRatio,
                  y = (mouseEvent.clientY - rect.top) * horizontalRatio,
                  altKey = mouseEvent.altKey,
                  metaKey = mouseEvent.ctrlKey || mouseEvent.metaKey
                )
              )
            }
            onWheelFunction = {
              val wheelEvent = it.asDynamic().nativeEvent as WheelEvent
              drawViewDelegate.dispatch(DrawView.Event.MouseWheel(wheelEvent.deltaY))
            }
          }
        }
      }
    }
  }

  private fun draw(circle: Circle?) {
    circle?.let {
      val canvas = document.getElementsByClassName("draw_canvas_${props.dependencies.cut.type.intType}")[0] as HTMLCanvasElement
      val context = canvas.getContext("2d") as CanvasRenderingContext2D
      context.clearRect(
        0.0,
        0.0,
        canvas.width.toDouble(),
        canvas.height.toDouble()
      )
      context.strokeStyle = "#00ff00"
      context.beginPath()
      context.arc(
        circle.dicomCenterX / verticalRatio,
        circle.dicomCenterY / horizontalRatio,
        circle.dicomRadius,
        0.0,
        2 * PI,
        false
      )
      context.stroke()
      context.closePath()
    }
  }


  private fun clearCanvas() {
    val canvas = document.getElementsByClassName("draw_canvas_${props.dependencies.cut.type.intType}")[0] as HTMLCanvasElement
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.clearRect(
      0.0,
      0.0,
      canvas.width.toDouble(),
      canvas.height.toDouble()
    )
  }

  private fun updateState(model: DrawView.Model) = setState { drawModel = model }
  private fun castEvent(it: Event): MouseEvent = it.asDynamic().nativeEvent as MouseEvent
  override fun componentWillUnmount() = lifecycleRegistry.destroy()

  interface Dependencies {
    val storeFactory: StoreFactory
    val cut: Cut
    val drawOutput: (DrawController.Output) -> Unit
    val researchId: Int
    val height: Int
    val width: Int
  }

}

class DrawState(
  var drawModel: DrawView.Model
) : RState

interface DrawProps : RProps {
  var dependencies: DrawComponent.Dependencies
}

fun RBuilder.drawView(
  dependencies: DrawComponent.Dependencies,
) = child(DrawComponent::class) {
  attrs.dependencies = dependencies
}
