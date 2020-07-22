package research.cut

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import controller.CutController
import controller.CutController.Input
import controller.SliderController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.border
import model.Cut
import org.w3c.dom.Element
import react.*
import react.dom.div
import react.dom.findDOMNode
import react.dom.render
import react.dom.unmountComponentAtNode
import repository.BrightnessRepository
import repository.MipRepository
import repository.ResearchRepository
import research.cut.CutContainer.CutContainerStyles.blackContainerStyle
import research.cut.CutContainer.CutContainerStyles.cutContainerStyle
import research.cut.CutContainer.CutContainerStyles.cutStyle
import research.cut.slider.SliderComponent
import research.cut.slider.sliderView
import root.debugLog
import styled.StyleSheet
import styled.css
import styled.styledDiv
import kotlin.browser.window

class CutContainer : RComponent<CutContainerProps, CutContainerState>() {

  private var testRef: Element? = null
  private val cutInput = BehaviorSubject<Input>(Input.Idle)
  private val disposable = CompositeDisposable()
  private var x = 0

  init {
    state = CutContainerState(width = 0, height = 0)
  }

  override fun componentDidMount() {
    disposable.add(props.dependencies.cutsInput.subscribe { cutInput.onNext(it) })
    window.addEventListener(type = "resize", callback = { callToRenderContent() })
  }

  override fun componentDidUpdate(
    prevProps: CutContainerProps,
    prevState: CutContainerState,
    snapshot: Any
  ) {
    callToRenderContent()
  }

  private fun callToRenderContent() {
    GlobalScope.launch {
      testRef?.let {
        renderContent(it.clientHeight, it.clientWidth)
      }
    }
  }

  override fun RBuilder.render() {
    styledDiv {
      css(cutContainerStyle)
      if (props.dependencies.cut.color.isNotEmpty()) {
        css {
          boxSizing = BoxSizing.borderBox
          border(2.px, BorderStyle.solid, Color(props.dependencies.cut.color))
        }
      }
      styledDiv {
        css(blackContainerStyle)
        styledDiv {
          css(cutStyle)
          if (x % 2 == 0) {
            div { }
          }

          ref {
            testRef = findDOMNode(it)
            if (testRef != null) {
              callToRenderContent()
            }
          }
        }
        if (props.dependencies.cut.data.n_images > 1) {
          styledDiv {
            css {
              width = 100.pct
              position = Position.relative
            }
            sliderView(
              dependencies = object : SliderComponent.Dependencies,
                Dependencies by props.dependencies {
                override val sliderOutput: (SliderController.Output) -> Unit = ::sliderOutput
              }
            )
          }
        }
      }
    }
  }

  private fun renderContent(clientHeight: Int, clientWidth: Int) {
    testRef?.let {
      render(it) {
        cut(
          dependencies = object : CutParentComponent.Dependencies,
            Dependencies by props.dependencies {
            override val cutsInput: Observable<Input> = this@CutContainer.cutInput
            override val height: Int = clientHeight
            override val width: Int = clientWidth
          }
        )
      }
    }
  }

  private fun sliderOutput(output: SliderController.Output) {
    when (output) {
      is SliderController.Output.SliceNumberChanged ->
        cutInput.onNext(Input.SliceNumberChanged(output.sliceNumber))
    }
  }

  override fun componentWillUnmount() {
    debugLog("MY: componentWillUnmount CutContainer ${props.dependencies.cut.type}")
    disposable.dispose()
    unmountComponentAtNode(testRef)
    testRef = null
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val cut: Cut
    val cutsInput: Observable<Input>
    val cutOutput: (CutController.Output) -> Unit
    val researchRepository: ResearchRepository
    val brightnessRepository: BrightnessRepository
    val mipRepository: MipRepository
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
  var height: Int,
  var width: Int
) : RState

interface CutContainerProps : RProps {
  var dependencies: CutContainer.Dependencies
}

fun RBuilder.cutContainer(
  dependencies: CutContainer.Dependencies
) = child(CutContainer::class) {
  attrs.dependencies = dependencies
}
