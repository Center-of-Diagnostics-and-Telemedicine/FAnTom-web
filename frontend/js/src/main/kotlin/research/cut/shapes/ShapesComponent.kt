package research.cut.shapes

import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.spacingUnits
import com.ccfraser.muirwik.components.themeContext
import kotlinx.css.*
import kotlinx.html.classes
import model.Cut
import model.blue
import model.pink
import model.yellow
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
import kotlin.math.roundToInt

class ShapesComponent(prps: ShapesProps) : RComponent<ShapesProps, ShapesState>(prps) {

  private var resultWidth: Int = 0
  private var resultHeight: Int = 0

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
//        state.areas?.let { drawCircles(it, context) }
//        state.moveRects?.let { drawRects(it, context) }
        drawLines(
          horizontal = props.shapesModel.horizontalCoefficient * resultHeight,
          vertical = props.shapesModel.verticalCoefficient * resultWidth,
          canvas = canvas,
          context = context
        )
      }
    }
  }

  override fun RBuilder.render() {
    themeContext.Consumer { theme ->
      val dicomWidth = props.cut.verticalCutData.data.maxFramesSize
      val dicomHeight = props.cut.data!!.height
      val ri = dicomWidth.toDouble() / dicomHeight
      val rs = props.width.toDouble() / props.height
      if (rs > ri) {
        debugLog("rs > ri")
        resultWidth = dicomWidth * props.height / dicomHeight
        resultHeight = props.height
      } else {
        debugLog("rs <= ri")
        resultWidth = props.width
        resultHeight = dicomHeight * props.width / dicomWidth
      }
      val mTop = props.height - resultHeight
      val mLeft = props.width - resultWidth
      val resultTop = if (mTop <= 0) 0 else mTop / 2
      val resultLeft = if (mLeft <= 0) 0 else mLeft / 2
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
          mTypography(text = "HU: $it")
        }
      }
    }
  }

//  private fun drawRects(
//    moveRects: List<MoveRect>,
//    context: CanvasRenderingContext2D
//  ) {
//    moveRects.forEach { rect ->
//      context.fillStyle = rect.color
//      context.fillRect(
//        rect.left,
//        rect.top,
//        rect.sideLength,
//        rect.sideLength
//      )
//    }
//  }
//
//  private fun drawCircles(
//    models: List<CircleShape>,
//    context: CanvasRenderingContext2D
//  ) {
//    models.forEach { model ->
//      if (model.highlight) {
//        context.strokeStyle = "#18a0fb"
//        context.lineWidth = 1.0
//
////        if (model.hasContext) {
////          context.lineWidth = 2.0
////        }
//
//      } else {
//        context.lineWidth = 1.0
//        context.strokeStyle = "#00ff00"
//      }
//
//      context.beginPath()
//      context.arc(
//        model.x,
//        model.y,
//        model.radius,
//        0.0,
//        2 * PI,
//        false
//      )
//      context.stroke()
//      context.closePath()
//    }
//  }

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
      strokeColor = props.cut.horizontalCutData.color,
      moveToX = 0.0,
      moveToY = horizontal,
      lineToX = width,
      lineToY = horizontal
    )

    drawLine(
      context = context,
      strokeColor = props.cut.verticalCutData.color,
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
