package research.cut

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.Observer
import com.arkivanov.mvikotlin.rx.observer
import controller.CutController
import controller.CutControllerImpl
import kotlinx.css.*
import model.Cut
import org.w3c.dom.Element
import react.*
import react.dom.findDOMNode
import repository.ResearchRepository
import research.cut.CutContainer.CutContainerStyles.blackContainerStyle
import research.cut.CutContainer.CutContainerStyles.canvasContainerStyle
import research.cut.CutContainer.CutContainerStyles.cutContainerStyle
import research.cut.CutContainer.CutContainerStyles.cutStyle
import resume
import styled.StyleSheet
import styled.css
import styled.styledDiv
import view.CutView
import view.SliderView
import view.initialCutModel
import view.initialSliderModel
import kotlin.browser.window

class CutContainer : RComponent<CutContainerProps, CutContainerState>() {

  private var testRef: Element? = null
  private val rBuilder = RBuilder()

  private val cutViewDelegate = CutViewProxy(::updateState)
  private val sliderViewDelegate = SliderViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: CutController

  init {
    state = CutContainerState(initialCutModel(), initialSliderModel())
  }

  override fun componentDidMount() {
    window.addEventListener(type = "resize", callback = {
      callToRenderContent()
    })
    lifecycleRegistry.resume()
    controller = createController()
    val dependencies = props.dependencies
    val disposable = dependencies.cutsInput(observer(onNext = controller.input))
    lifecycleRegistry.doOnDestroy(disposable::dispose)
    controller.onViewCreated(cutViewDelegate, sliderViewDelegate, lifecycleRegistry)
  }

  private fun createController(): CutController {
    val dependencies = props.dependencies
    val cutControllerDependencies =
      object : CutController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
      }
    return CutControllerImpl(cutControllerDependencies)
  }

  override fun componentDidUpdate(
    prevProps: CutContainerProps,
    prevState: CutContainerState,
    snapshot: Any
  ) {
    callToRenderContent()
  }

  private fun callToRenderContent() {
    testRef?.let { renderContent(it.clientHeight, it.clientWidth) }
  }

  override fun RBuilder.render() {
    styledDiv {
      css(cutContainerStyle)

//      attrs {
//        onDoubleClickFunction = { props.doubleClickListener(cellModel) }
//      }

      //внутренний контейнер с фоном
      //тут лежит и слайдер
      styledDiv {
        css(blackContainerStyle)
//        css {
//          border = "4px solid ${Color(getColorByCutType(cellModel.cutType)).withAlpha(0.7)}"
//        }
        styledDiv {
          css(cutStyle)

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
          slider(
            sliceNumber = state.sliderModel.currentValue,
            defaultValue = state.sliderModel.defaultValue,
            maxValue = state.sliderModel.maxValue,
            onChange = ::handleChangeSliceNumber
          )
        }
      }
    }
  }

  private fun renderContent(clientHeight: Int, clientWidth: Int) {
    react.dom.render(
      element = rBuilder
        //контейнер канвасов
        .styledDiv {
          css(canvasContainerStyle)
          cut(
            state.cutModel.slice,
            h = clientHeight,
            w = clientWidth
          )
//          shapesCanvas(clientHeight, clientWidth, cellModel)
//          drawCanvas(cellModel.cutType, clientHeight, clientWidth, cellModel.sliceSizeData)
        }.asElementOrNull(),
      container = testRef
    )
  }

  private fun handleChangeSliceNumber(sliceNumber: Int) {
    sliderViewDelegate.dispatch(SliderView.Event.HandleOnChange(sliceNumber))
  }

  private fun updateState(model: CutView.Model) = setState { cutModel = model }
  private fun updateState(model: SliderView.Model) = setState { sliderModel = model }

  interface Dependencies {
    val storeFactory: StoreFactory
    val cut: Cut
    val cutsInput: (Observer<CutController.Input>) -> Disposable
    val cutOutput: (CutController.Output) -> Unit
    val researchRepository: ResearchRepository
    val researchId: Int
  }

  object CutContainerStyles : StyleSheet("CutStyles", isStatic = true) {
    val cutContainerStyle by css {
      flex(1.0)
      display = Display.flex
      flexDirection = FlexDirection.column
      position = Position.relative
    }

    val blackContainerStyle by css {
      flex(1.0)
      display = Display.flex
      flexDirection = FlexDirection.column
      background = "#000"
      textAlign = TextAlign.center
      position = Position.relative
    }

    val canvasContainerStyle by css {
      position = Position.absolute
      top = 0.px
      left = 0.px
      display = Display.flex
    }
    val cutStyle by css {
      flex(1.0)
      display = Display.flex
      flexDirection = FlexDirection.column
      position = Position.relative
      overflow = Overflow.hidden
    }
  }
}

class CutContainerState(
  var cutModel: CutView.Model,
  var sliderModel: SliderView.Model
) : RState

interface CutContainerProps : RProps {
  var dependencies: CutContainer.Dependencies
}

fun RBuilder.cutContainer(
  dependencies: CutContainer.Dependencies
) = child(CutContainer::class) {
  attrs.dependencies = dependencies
}
