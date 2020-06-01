package research.cut.shapes

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.spacingUnits
import com.ccfraser.muirwik.components.themeContext
import controller.ShapesController
import controller.ShapesControllerImpl
import destroy
import kotlinx.css.*
import kotlinx.html.classes
import model.Cut
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.get
import react.*
import repository.ResearchRepository
import resume
import root.debugLog
import styled.css
import styled.styledCanvas
import styled.styledDiv
import view.ShapesView
import view.initialShapesModel
import kotlin.browser.document

class ShapesComponent(prps: ShapesProps) : RComponent<ShapesProps, ShapesState>(prps) {

  private val shapesViewDelegate = ShapesViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: ShapesController
  var resultWidth: Int = 0
  var resultHeight: Int = 0

  init {
    state = ShapesState(initialShapesModel())
  }

  override fun componentDidMount() {
    lifecycleRegistry.resume()
    controller = createController()
    val dependencies = props.dependencies
    val disposable = dependencies.shapesInput.subscribe { controller.input(it) }
    lifecycleRegistry.doOnDestroy(disposable::dispose)
    controller.onViewCreated(shapesViewDelegate, lifecycleRegistry)
    updateCanvas()
  }

  override fun componentDidUpdate(prevProps: ShapesProps, prevState: ShapesState, snapshot: Any) {

    updateCanvas()
  }

  private fun createController(): ShapesController {
    val dependencies = props.dependencies
    val cutControllerDependencies =
      object : ShapesController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
      }
    return ShapesControllerImpl(cutControllerDependencies)
  }

  private fun updateCanvas() {
    val canvas = document.getElementsByClassName("lines_canvas_${props.dependencies.cut.type.intType}")[0] as? HTMLCanvasElement
    canvas?.let { _ ->
      val context = canvas.getContext("2d") as? CanvasRenderingContext2D
      context?.let { _ ->
        clearCanvas(canvas, context)
//        state.areas?.let { drawCircles(it, context) }
//        state.moveRects?.let { drawRects(it, context) }
        debugLog("width = ${props.dependencies.width}")
        drawLines(
          horizontal = state.shapesModel.horizontalCoefficient * resultHeight,
          vertical = state.shapesModel.verticalCoefficient * resultWidth,
          canvas = canvas,
          context = context
        )
      }
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
      styledDiv {
        css {
          position = Position.absolute
          zIndex = 1
          top = resultTop.px
          left = resultLeft.px
        }
        styledCanvas {
          attrs {
            classes += "lines_canvas_${props.dependencies.cut.type.intType}"
            width = resultWidth.toString()
            height = resultHeight.toString()
          }
        }
      }

//      styledDiv {
//        css {
//          position = Position.absolute
//          zIndex = 1
//          bottom = 0.px
//          left = 0.px
//          padding(1.spacingUnits)
//        }
//        state.positionData?.let {
//          mTypography(text = "Сагиттальный (x): ${it.x}") {
//            css {
//              color = Color(blue)
//            }
//          }
//          mTypography(text = "Фронтальный (y): ${it.y}") {
//            css {
//              color = Color(pink)
//            }
//          }
//          mTypography(text = " Аксиальный(z): ${it.z}") {
//            css {
//              color = Color(yellow)
//            }
//          }
//        }
//      }

//      val cutTypeModelContainer = props.dependencies.cut.cutTypeModelContainer
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
        if (state.shapesModel.sliceNumber != 0) {
          mTypography(text = "Срез: ${state.shapesModel.sliceNumber}")
        }
      }

      state.shapesModel.huValue?.let {
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
      strokeColor = props.dependencies.cut.horizontalCutData.color,
      moveToX = 0.0,
      moveToY = horizontal,
      lineToX = width,
      lineToY = horizontal
    )

    drawLine(
      context = context,
      strokeColor = props.dependencies.cut.verticalCutData.color,
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
    context.clearRect(
      0.0,
      0.0,
      canvas.width.toDouble(),
      canvas.height.toDouble()
    )
  }

  private fun updateState(model: ShapesView.Model) = setState { shapesModel = model }

  override fun componentWillUnmount() {
    lifecycleRegistry.destroy()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val researchRepository: ResearchRepository
    val cut: Cut
    val shapesOutput: (ShapesController.Output) -> Unit
    val shapesInput: Observable<ShapesController.Input>
    val researchId: Int
    val height: Int
    val width: Int
  }

}

class ShapesState(
  var shapesModel: ShapesView.Model
) : RState

interface ShapesProps : RProps {
  var dependencies: ShapesComponent.Dependencies
}

fun RBuilder.shapesView(
  dependencies: ShapesComponent.Dependencies,
) = child(ShapesComponent::class) {
  attrs.dependencies = dependencies
}
