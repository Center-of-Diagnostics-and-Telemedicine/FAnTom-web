package research.cut.shapes

import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.spacingUnits
import com.ccfraser.muirwik.components.themeContext
import kotlinx.css.*
import kotlinx.css.Position
import kotlinx.html.classes
import model.*
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.get
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import root.debugLog
import styled.css
import styled.styledCanvas
import styled.styledDiv
import view.ShapesView
import kotlin.browser.document
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class ShapesComponent(prps: ShapesProps) : RComponent<ShapesProps, ShapesState>(prps) {

  private var resultWidth: Int = 0
  private var resultHeight: Int = 0
  private var horizontalRatio: Double = 0.0
  private var verticalRatio: Double = 0.0
  private var radiusRatio: Double = 0.0

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
        props.shapesModel.circles?.let { drawCircles(it, context) }
//        state.moveRects?.let { drawRects(it, context) }
        drawLines(
          horizontal = props.shapesModel.verticalCoefficient * resultHeight,
          vertical = props.shapesModel.horizontalCoefficient * resultWidth,
          canvas = canvas,
          context = context
        )
      }
    }
  }

  override fun RBuilder.render() {
    themeContext.Consumer { theme ->
      val dicomWidth = props.cut.data!!.screen_size_h
      val dicomHeight = props.cut.data!!.screen_size_v
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

      styledDiv {
        css {
          position = Position.absolute
          zIndex = 1
          bottom = 0.px
          left = 0.px
          padding(1.spacingUnits)
        }
        props.shapesModel.position?.let {
          mTypography(text = "Сагиттальный (x): ${it.x.roundToInt()}") {
            css { color = Color(blue) }
          }
          mTypography(text = "Фронтальный (y): ${it.y.roundToInt()}") {
            css { color = Color(pink) }
          }
          mTypography(text = "Аксиальный(z): ${it.z.roundToInt()}") {
            css { color = Color(yellow) }
          }
        }
      }

//      val cutTypeModelContainer = props.cut.cutTypeModelContainer
//      val otherTypes = cutTypeModelContainer.availableOtherTypesForCut
//
//      styledDiv {
//        css {
//          position = Position.absolute
//          zIndex = 10
//          top = 0.px
//          left = 0.px
//          padding(1.spacingUnits)
//        }
//        mClickAwayListener(onClickAway = { setState { showMenu = false } }) {
//          mList(disablePadding = true) {
//            css {
//              backgroundColor = Color("#424242")
//              borderRadius = 4.px
//
//            }
//            mListItem(
//              cutTypeModelContainer.currentCutType.name,
//              onClick = { setState { showMenu = !showMenu } },
//              divider = false
//            ) {
//              if (otherTypes.isNotEmpty()) {
//                if (state.showMenu) mIcon("expand_less") else mIcon("expand_more")
//              }
//            }
//            mCollapse(show = state.showMenu) {
//              mList {
//                otherTypes.forEach { cutType ->
//                  mListItem(
//                    button = true,
//                    onClick = { handleSimpleClick(cutType) },
//                    alignItems = MListItemAlignItems.flexStart
//                  ) {
//                    mTypography(text = cutType.name)
//                  }
//                }
//              }
//            }
//          }
//        }
//      }


      styledDiv {
        css {
          position = Position.absolute
          zIndex = 1
          top = 0.px
          right = 0.px
          padding(1.spacingUnits)
        }
        if (props.shapesModel.sliceNumber != 0) {
          mTypography(text = "Срез: ${props.shapesModel.sliceNumber}") {
            css { color = Color.white }
          }
        }
      }

      props.shapesModel.huValue?.let {
        styledDiv {
          css {
            position = Position.absolute
            zIndex = 1
            bottom = 0.px
            right = 0.px
            padding(1.spacingUnits)
          }
          mTypography(text = "HU: $it") {
            css { color = Color.white }
          }
        }
      }
    }
  }

//  private fun drawMarks(
//    moveRects: List<MarkDomain>,
//    context: CanvasRenderingContext2D
//  ) {
//    moveRects.forEach { rect ->
////      context.fillStyle = rect.color
//      context.fillRect(
//        rect.left,
//        rect.top,
//        rect.sideLength,
//        rect.sideLength
//      )
//    }
//  }

  private fun drawCircles(
    models: List<Circle>,
    context: CanvasRenderingContext2D
  ) {
    models.forEach { circle ->
      debugLog(circle.toString())
      if (circle.highlight) {
        context.strokeStyle = "#18a0fb"
        context.lineWidth = 1.0

//        if (circle.hasContext) {
//          context.lineWidth = 2.0
//        }

      } else {
        context.lineWidth = 1.0
        context.strokeStyle = "#00ff00"
      }

      context.beginPath()
      context.arc(
        circle.dicomCenterX / horizontalRatio,
        circle.dicomCenterY / verticalRatio,
        circle.dicomRadius / radiusRatio,
        0.0,
        2 * PI,
        false
      )
      context.stroke()
      context.closePath()
    }
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
    strokeColor: String,
    moveToX: Double,
    moveToY: Double,
    lineToX: Double,
    lineToY: Double
  ) {
    context.beginPath()
    context.strokeStyle = strokeColor
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
) : RState

interface ShapesProps : RProps {
  var cut: Cut
  var width: Int
  var height: Int
  var shapesModel: ShapesView.Model
  var eventOutput: (ShapesView.Event) -> Unit
}

fun RBuilder.shapesView(
  cut: Cut,
  width: Int,
  height: Int,
  shapesModel: ShapesView.Model,
  eventOutput: (ShapesView.Event) -> Unit
) = child(ShapesComponent::class) {
  attrs.cut = cut
  attrs.width = width
  attrs.height = height
  attrs.shapesModel = shapesModel
  attrs.eventOutput = eventOutput
}
