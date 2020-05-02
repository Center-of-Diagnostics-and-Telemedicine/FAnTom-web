package presentation.screen.research.cut

import com.badoo.reaktive.subject.publish.PublishSubject
import Constants
import client.*
import client.newmvi.shapes.binder.ShapesBinder
import client.newmvi.shapes.view.ShapesView
import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.list.MListItemAlignItems
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.transitions.mCollapse
import presentation.di.injectShapesCanvas
import model.*
import kotlinx.css.*
import kotlinx.css.Position
import kotlinx.html.classes
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.get
import react.*
import styled.css
import styled.styledCanvas
import styled.styledDiv
import kotlin.browser.document
import kotlin.math.PI

class MviShapesCanvasComponent(props: MviShapesCanvasProps) :
  RComponent<MviShapesCanvasProps, MviShapesCanvasState>(props),
  ShapesView {

  override val events: PublishSubject<ShapesView.Event> = PublishSubject()
  private lateinit var binder: ShapesBinder

  override fun MviShapesCanvasState.init(props: MviShapesCanvasProps) {
    circles = listOf()
    horizontalLine = initialLine(props.cellModel.cutType, LineType.HORIZONTAL)
    verticalLine = initialLine(props.cellModel.cutType, LineType.VERTICAL)
    selectedMenuIndex = when (props.cellModel.cutType) {
      SLYCE_TYPE_AXIAL -> 0
      SLYCE_TYPE_FRONTAL -> 1
      SLYCE_TYPE_SAGITTAL -> 2
      else -> 0
    }
    showMenu = false
    binder = injectShapesCanvas(props.cellModel.cutType)
  }

  override fun componentDidMount() {
    binder.detachView()
    binder.onStop()
    binder.attachView(this)
    binder.onStart()
    updateCanvas()
  }

  override fun componentDidUpdate(
    prevProps: MviShapesCanvasProps,
    prevState: MviShapesCanvasState,
    snapshot: Any
  ) {
    if (prevProps.cellModel.cutType != props.cellModel.cutType) {
      binder.detachView()
      binder.onStop()
      binder = injectShapesCanvas(props.cellModel.cutType)
      binder.attachView(this)
      binder.onStart()
    }

    updateCanvas()
  }

  override fun show(model: ShapesView.ShapesViewModel) {
    setState {
      sliceNumber = model.sliceNumber
      positionData = model.positionData
      huValue = model.huValue
      areas = model.areas
      moveRects = model.moveRects
      horizontalLine = model.horizontal
      verticalLine = model.vertical
    }
  }

  private fun updateCanvas() {
    val canvas = document.getElementsByClassName("lines_canvas_${props.cellModel.cutType}")[0] as? HTMLCanvasElement
    canvas?.let { _ ->
      val context = canvas.getContext("2d") as? CanvasRenderingContext2D
      context?.let { _ ->
        clearCanvas(canvas, context)
        state.areas?.let { drawCircles(it, context) }
        state.moveRects?.let { drawRects(it, context) }
        drawLines(state.horizontalLine, state.verticalLine, canvas, context)
      }
    }
  }

  override fun RBuilder.render() {
    themeContext.Consumer { theme ->

      val ri = 512.0 / props.cellModel.sliceSizeData.height
      val rs = props.width.toDouble() / props.height
      val resultWidth: Int
      val resultHeight: Int
      if (rs > ri) {
        resultWidth = 512 * props.height / props.cellModel.sliceSizeData.height
        resultHeight = props.height
      } else {
        resultWidth = props.width
        resultHeight = props.cellModel.sliceSizeData.height * props.width / 512
      }
      val resultTop = (props.height - resultHeight) / 2
      val resultLeft = (props.width - resultWidth) / 2
      styledDiv {
        css {
          position = Position.absolute
          zIndex = 1
          top = resultTop.px
          left = resultLeft.px
        }
        styledCanvas {
          attrs {
            classes += "lines_canvas lines_canvas_${props.cellModel.cutType}"
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
        state.positionData?.let {
          mTypography(text = "Сагиттальный (x): ${it.x}") {
            css {
              color = Color(blue)
            }
          }
          mTypography(text = "Фронтальный (y): ${it.y}") {
            css {
              color = Color(pink)
            }
          }
          mTypography(text = " Аксиальный(z): ${it.z}") {
            css {
              color = Color(yellow)
            }
          }
        }
      }

      val cutTypeModelContainer = props.cellModel.cutTypeModelContainer
      val otherTypes = cutTypeModelContainer.availableOtherTypesForCut

      styledDiv {
        css {
          position = Position.absolute
          zIndex = 10
          top = 0.px
          left = 0.px
          padding(1.spacingUnits)
        }
        mClickAwayListener(onClickAway = { setState { showMenu = false } }) {
          mList(disablePadding = true) {
            css {
              backgroundColor = Color("#424242")
              borderRadius = 4.px

            }
            mListItem(
              cutTypeModelContainer.currentCutType.name,
              onClick = { setState { showMenu = !showMenu } },
              divider = false
            ) {
              if (otherTypes.isNotEmpty()) {
                if (state.showMenu) mIcon("expand_less") else mIcon("expand_more")
              }
            }
            mCollapse(show = state.showMenu) {
              mList {
                otherTypes.forEach { cutType ->
                  mListItem(
                    button = true,
                    onClick = { handleSimpleClick(cutType) },
                    alignItems = MListItemAlignItems.flexStart
                  ) {
                    mTypography(text = cutType.name)
                  }
                }
              }
            }
          }
        }
      }


      styledDiv {
        css {
          position = Position.absolute
          zIndex = 1
          top = 0.px
          right = 0.px
          padding(1.spacingUnits)
        }
        if (state.sliceNumber != 0) {
          mTypography(text = "Срез: ${state.sliceNumber}")
        }
      }

      state.huValue?.let {
        styledDiv {
          css {
            position = Position.absolute
            zIndex = 1
            bottom = 0.px
            right = 0.px
            padding(1.spacingUnits)
          }
          mTypography(text = "HU: ${state.huValue}")
        }
      }
    }
  }

  private fun handleSimpleClick(type: CutType) {
    dispatch(ShapesView.Event.ChangeCutType(type, props.cellModel))
    setState { showMenu = false }
  }

  private fun drawRects(
    moveRects: List<MoveRect>,
    context: CanvasRenderingContext2D
  ) {
    moveRects.forEach { rect ->
      context.fillStyle = rect.color
      context.fillRect(
        rect.left,
        rect.top,
        rect.sideLength,
        rect.sideLength
      )
    }
  }

  private fun drawCircles(
    models: List<CircleShape>,
    context: CanvasRenderingContext2D
  ) {
    models.forEach { model ->
      if (model.highlight) {
        context.strokeStyle = "#18a0fb"
        context.lineWidth = 1.0

//        if (model.hasContext) {
//          context.lineWidth = 2.0
//        }

      } else {
        context.lineWidth = 1.0
        context.strokeStyle = "#00ff00"
      }

      context.beginPath()
      context.arc(
        model.x,
        model.y,
        model.radius,
        0.0,
        2 * PI,
        false
      )
      context.stroke()
      context.closePath()
    }
  }

  private fun drawLines(
    horizontal: Line,
    vertical: Line,
    canvas: HTMLCanvasElement,
    context: CanvasRenderingContext2D
  ) {
    val height = canvas.height.toDouble()
    val width = canvas.width.toDouble()

    val y = horizontal.value.toDouble()
    drawLine(context, horizontal.color, Constants.ZERO, y, width, y)

    val x = vertical.value.toDouble()
    drawLine(context, vertical.color, x, Constants.ZERO, x, height)
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

  override fun dispatch(event: ShapesView.Event) {
    events.onNext(event)
  }
}

interface MviShapesCanvasProps : RProps {
  var height: Int
  var width: Int
  var cellModel: CellModel
}

interface MviShapesCanvasState : RState {
  var circles: List<CircleShape>
  var horizontalLine: Line
  var verticalLine: Line
  var positionData: PositionData?
  var sliceNumber: Int
  var huValue: Double?
  var areas: List<CircleShape>?
  var moveRects: List<MoveRect>?
  var selectedMenuIndex: Int
  var showMenu: Boolean
}

fun RBuilder.mviShapesCanvas(
  height: Int,
  width: Int,
  cellModel: CellModel
) = child(MviShapesCanvasComponent::class) {
  attrs.height = height
  attrs.width = width
  attrs.cellModel = cellModel
}
