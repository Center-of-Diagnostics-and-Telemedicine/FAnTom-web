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
import styled.css
import styled.styledCanvas
import styled.styledDiv
import view.ShapesView
import kotlinx.browser.document
import react.dom.attrs
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
          props.shapesModel.shapes.let { drawShapes(it, context) }
          props.shapesModel.rects.let { drawRects(it, context) }
          if (props.cut.data.nImages > 1) {
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
      val dicomWidth = props.cut.data.screenSizeH
      val dicomHeight = props.cut.data.screenSizeV
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
        imagesCount = props.cut.data.nImages
      )

      when (props.cut.researchType) {
        ResearchType.CT -> {
          props.shapesModel.huValue?.let {
            huValue(it)
          }
        }
        ResearchType.MG,
        ResearchType.DX -> {
          props.shapesModel.huValue?.let {
            brightnessValue(it)
          }
        }
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
        this@styledCanvas.attrs {
          classes = classes + "shape_canvas_${props.cut.type.intType}"
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

  private fun drawShapes(
    models: List<Shape>,
    context: CanvasRenderingContext2D
  ) {
    models.forEach { shape ->
      context.beginPath()
      when (shape) {
        is CircleModel -> {
          if (props.cut.isPlanar()) {
            drawPlanarCircle(shape, context)
          } else {
            drawSphere(context, shape)
          }
        }
        is RectangleModel -> {
          drawPlanarRectangle(shape, context)
        }
      }
      context.stroke()
      context.closePath()
    }
  }

  private fun drawPlanarCircle(circle: CircleModel, context: CanvasRenderingContext2D) {
    val color = circle.color
    context.strokeStyle = if (color.isEmpty()) defaultMarkColor else color
    if (circle.highlight) {
      context.lineWidth = 2.0
    } else {
      context.lineWidth = 1.0
    }

    val radiusX = circle.dicomWidth / horizontalRatio
    val radiusY = circle.dicomHeight / verticalRatio
    val centerX = circle.dicomX / horizontalRatio
    val centerY = circle.dicomY / verticalRatio
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

  private fun drawPlanarRectangle(rectangle: RectangleModel, context: CanvasRenderingContext2D) {
    val color = rectangle.color
    context.fillStyle = if (color.isEmpty()) defaultMarkColor else color

    val x = (rectangle.dicomX - rectangle.dicomWidth) / horizontalRatio
    val y = (rectangle.dicomY - rectangle.dicomHeight) / verticalRatio
    val w = rectangle.dicomWidth / horizontalRatio * 2
    val h = rectangle.dicomHeight / verticalRatio * 2
    context.globalAlpha = if (rectangle.highlight) 0.5 else 0.3
    context.fillRect(x, y, w, h)
    context.globalAlpha = 1.0

    if (rectangle.highlight) {
      context.strokeStyle = if (color.isEmpty()) defaultMarkColor else color
      context.lineWidth = 2.0
      context.rect(x, y, w, h)
    }
  }

  private fun drawSphere(context: CanvasRenderingContext2D, circle: CircleModel) {
    val color = circle.color
    context.strokeStyle = if (color.isEmpty()) defaultMarkColor else color
    if (circle.highlight) {
      context.lineWidth = 2.0
    } else {
      context.lineWidth = 1.0
    }
    context.arc(
      circle.dicomX / horizontalRatio,
      circle.dicomY / verticalRatio,
      circle.dicomWidth / radiusRatio,
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
    context.lineWidth = 1.0
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

}

class ShapesState(
  var showMenu: Boolean
) : RState

interface ShapesProps : RProps {
  var cut: Plane
  var width: Int
  var height: Int
  var shapesModel: ShapesView.Model
  var eventOutput: (ShapesView.Event) -> Unit
  var loading: Boolean
}

fun RBuilder.shapesView(
  cut: Plane,
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
