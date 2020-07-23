package research.cut.shapes

import com.ccfraser.muirwik.components.mCssBaseline
import com.ccfraser.muirwik.components.themeContext
import kotlinx.css.*
import kotlinx.css.Position
import kotlinx.html.classes
import model.*
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.get
import react.*
import root.debugLog
import styled.css
import styled.styledCanvas
import styled.styledDiv
import view.ShapesView
import kotlin.browser.document
import kotlin.math.*

class ShapesComponent(prps: ShapesProps) : RComponent<ShapesProps, ShapesState>(prps) {

  private var resultWidth: Int = 0
  private var resultHeight: Int = 0
  private var horizontalRatio: Double = 0.0
  private var verticalRatio: Double = 0.0
  private var radiusRatio: Double = 0.0

  init {
    state = ShapesState(false)
  }

  override fun componentDidMount() {
    updateCanvas()
  }

  override fun componentDidUpdate(prevProps: ShapesProps, prevState: ShapesState, snapshot: Any) {
    updateCanvas()
  }

  private fun updateCanvas() {
    val canvas = document.getElementsByClassName("shape_canvas_${props.cut.type.intType}")[0] as? HTMLCanvasElement
    canvas?.let { _ ->
      val context = canvas.getContext("2d") as? CanvasRenderingContext2D
      context?.let { _ ->
        clearCanvas(canvas, context)
        if (props.loading.not()) {
          props.shapesModel.circles.let { drawCircles(it, context) }
          props.shapesModel.rects.let { drawRects(it, context) }
          if (props.cut.data.n_images > 1) {
            drawLines(
              horizontal = props.shapesModel.verticalCoefficient * resultHeight,
              vertical = props.shapesModel.horizontalCoefficient * resultWidth,
              canvas = canvas,
              context = context
            )
          }
        }
      }
    }
  }

  override fun RBuilder.render() {
    mCssBaseline()
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
      horizontalRatio = dicomHeight.toDouble() / resultHeight
      verticalRatio = dicomWidth.toDouble() / resultWidth
      val dicomRadius = sqrt(dicomHeight.toDouble().pow(2) + dicomWidth.toDouble().pow(2))
      val screenRadius = sqrt(resultHeight.toDouble().pow(2) + resultWidth.toDouble().pow(2))
      radiusRatio = dicomRadius / screenRadius

      shapesCanvas(resultTop, resultLeft)
      props.shapesModel.position?.let { pointPosition(it) }

//      val otherTypes = props.cut.availableCutsForChange
//      val cutName = props.cut.type.getName()
//      cutType(
//        cutName = cutName,
//        otherTypes = otherTypes,
//        onClickAway = { setState { showMenu = false } },
//        onClick = { setState { showMenu = !showMenu } },
//        showMenu = state.showMenu,
//        onMenuItemClick = { handleSimpleClick(it) }
//      )

      sliceNumber(
        sliceNumber = props.shapesModel.sliceNumber,
        imagesCount = props.cut.data.n_images
      )

      props.shapesModel.huValue?.let {
        huValue(it)
      }
    }
  }

  private fun RBuilder.shapesCanvas(resultTop: Int, resultLeft: Int) {
    styledDiv {
      css {
        position = Position.absolute
        zIndex = 1
        top = resultTop.px
        left = resultLeft.px
      }
      styledCanvas {
        attrs {
          classes += "shape_canvas_${props.cut.type.intType}"
          width = resultWidth.toString()
          height = resultHeight.toString()
        }
      }
    }
  }

  private fun handleSimpleClick(type: CutType) {
    props.eventOutput(ShapesView.Event.CutTypeOnChange(type))
    setState { showMenu = false }
  }

  private fun drawRects(
    moveRects: List<Rect>,
    context: CanvasRenderingContext2D
  ) {
    moveRects.forEach { rect ->
      context.fillStyle = "#fff"
      context.fillRect(
        (rect.left / horizontalRatio) - rect.sideLength / horizontalRatio / 2,
        (rect.top / verticalRatio) - rect.sideLength / verticalRatio / 2,
        rect.sideLength / horizontalRatio,
        rect.sideLength / verticalRatio
      )
    }
  }

  private fun drawCircles(
    models: List<Circle>,
    context: CanvasRenderingContext2D
  ) {
    models.forEach { circle ->
      context.beginPath()
      if (props.cut.isPlanar()) {
        drawPlanarCircle(circle, context)
      } else {
        drawSphere(context, circle)
      }
      context.stroke()
      context.closePath()
    }
  }

  private fun drawPlanarCircle(circle: Circle, context: CanvasRenderingContext2D) {
    context.lineWidth = 1.0
    if (circle.highlight) {
      context.strokeStyle = props.cut.color
    } else {
      context.strokeStyle = "#00ff00"
    }

    val radiusX = circle.dicomRadiusHorizontal / horizontalRatio
    val radiusY = circle.dicomRadiusVertical / verticalRatio
    val centerX = circle.dicomCenterX / horizontalRatio
    val centerY = circle.dicomCenterY / verticalRatio
    val step = 0.01
    var a = step
    val pi2 = PI * 2 - step

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
  }

  private fun drawSphere(context: CanvasRenderingContext2D, circle: Circle) {
    if (circle.highlight) {
      context.strokeStyle = "#18a0fb"
      context.lineWidth = 1.0
    } else {
      context.lineWidth = 1.0
      context.strokeStyle = "#00ff00"
    }
    context.arc(
      circle.dicomCenterX / horizontalRatio,
      circle.dicomCenterY / verticalRatio,
      circle.dicomRadiusHorizontal / radiusRatio,
      0.0,
      2 * PI,
      false
    )
  }

  private fun drawLines(
    horizontal: Double,
    vertical: Double,
    canvas: HTMLCanvasElement,
    context: CanvasRenderingContext2D
  ) {
//    if (horizontal != 0.0 && vertical != 0.0) {
    val height = canvas.height.toDouble()
    val width = canvas.width.toDouble()

    drawLine(
      context = context,
      strokeColor = props.cut.verticalCutData?.color,
      moveToX = 0.0,
      moveToY = horizontal,
      lineToX = width,
      lineToY = horizontal
    )

    drawLine(
      context = context,
      strokeColor = props.cut.horizontalCutData?.color,
      moveToX = vertical,
      moveToY = 0.0,
      lineToX = vertical,
      lineToY = height
    )
//    }
  }

  private fun drawLine(
    context: CanvasRenderingContext2D,
    strokeColor: String?,
    moveToX: Double,
    moveToY: Double,
    lineToX: Double,
    lineToY: Double
  ) {
    context.beginPath()
    strokeColor?.let { context.strokeStyle = it }
    context.moveTo(moveToX, moveToY)
    context.lineTo(lineToX, lineToY)
    context.stroke()
    context.closePath()
  }

  private fun clearCanvas(
    canvas: HTMLCanvasElement,
    context: CanvasRenderingContext2D
  ) {
    context.clearRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())
  }

  override fun componentWillUnmount() {
    debugLog("MY: componentWillUnmount ShapesComponent ${props.cut.type}")
  }

}

class ShapesState(
  var showMenu: Boolean
) : RState

interface ShapesProps : RProps {
  var cut: Cut
  var width: Int
  var height: Int
  var shapesModel: ShapesView.Model
  var eventOutput: (ShapesView.Event) -> Unit
  var loading: Boolean
}

fun RBuilder.shapesView(
  cut: Cut,
  width: Int,
  height: Int,
  shapesModel: ShapesView.Model,
  eventOutput: (ShapesView.Event) -> Unit,
  loading: Boolean
) = child(ShapesComponent::class) {
  attrs.cut = cut
  attrs.width = width
  attrs.height = height
  attrs.shapesModel = shapesModel
  attrs.eventOutput = eventOutput
  attrs.loading = loading
}
