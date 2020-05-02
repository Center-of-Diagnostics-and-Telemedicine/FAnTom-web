package presentation.screen.research.cut

import client.newmvi.ResearchContainer
import presentation.screen.ComponentStyles
import model.CellModel
import model.ContainerSizeModel
import model.getColorByCutType
import kotlinx.css.*
import kotlinx.css.Position
import kotlinx.html.js.onDoubleClickFunction
import model.*
import org.w3c.dom.Element
import react.*
import react.dom.findDOMNode
import styled.css
import styled.styledDiv
import kotlin.browser.window

class CutContainer : RComponent<CutContainerProps, RState>() {

  private var testRef: Element? = null

  override fun componentDidMount() {
    window.addEventListener(type = "resize", callback = {
      callToRenderContent()
    })
  }

  override fun componentDidUpdate(prevProps: CutContainerProps, prevState: RState, snapshot: Any) {
    callToRenderContent()
  }

  private fun callToRenderContent() {
    testRef?.let {
      val model = ContainerSizeModel(it.clientWidth, it.clientHeight)
      when (props.cellModel.cutType) {
        SLYCE_TYPE_AXIAL -> {
          ResearchContainer.axialContainerSizeChangeListener.onNext(model)
        }
        SLYCE_TYPE_FRONTAL -> {
          ResearchContainer.frontalContainerSizeChangeListener.onNext(model)
        }
        SLYCE_TYPE_SAGITTAL -> {
          ResearchContainer.sagittalContainerSizeChangeListener.onNext(model)
        }
      }

      renderContent(it.clientHeight, it.clientWidth)
    }
  }

  override fun RBuilder.render() {
    val cellModel = props.cellModel
    styledDiv {
      css(ComponentStyles.cutContainerStyle)

      attrs {
        onDoubleClickFunction = { props.doubleClickListener(cellModel) }
      }

      //внутренний контейнер с фоном
      //тут лежит и слайдер
      styledDiv {
        css(ComponentStyles.blackContainerStyle)
        css {
          border = "4px solid ${Color(getColorByCutType(cellModel.cutType)).withAlpha(0.7)}"
        }
        styledDiv {
          css {
            flex(1.0)
            display = Display.flex
            flexDirection = FlexDirection.column
            position = Position.relative
            overflow = Overflow.hidden
          }

          ref {
            testRef = findDOMNode(it)
            if (testRef != null) {
              callToRenderContent()
            }
          }
        }
        styledDiv {
          css {
            width = 100.pct
            position = Position.relative
          }
          mviSlider(cellModel.cutType, cellModel.sliceSizeData.maxFramesSize)
        }
      }
    }
  }

  private fun renderContent(clientHeight: Int, clientWidth: Int) {
    val cellModel = props.cellModel
    react.dom.render(
      element = RBuilder()
        //контейнер канвасов
        .styledDiv {
          css {
            position = Position.absolute
            top = 0.px
            left = 0.px
            display = Display.flex
          }
          mviCut(cellModel.cutType, clientHeight, clientWidth)
          mviShapesCanvas(
            clientHeight,
            clientWidth,
            cellModel
          )
          mviDrawCanvas(cellModel.cutType, clientHeight, clientWidth, cellModel.sliceSizeData)
        }.asElementOrNull(),
      container = testRef
    )
  }
}

interface CutContainerProps : RProps {
  var cellModel: CellModel
  var doubleClickListener: (CellModel) -> Unit
}

fun RBuilder.cutContainer(
  cellModel: CellModel,
  doubleClickListener: (CellModel) -> Unit
) = child(CutContainer::class) {
  attrs.cellModel = cellModel
  attrs.doubleClickListener = doubleClickListener
}