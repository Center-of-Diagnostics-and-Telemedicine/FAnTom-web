package research.cut

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.publish.PublishSubject
import controller.CutController
import controller.CutController.Input
import controller.SliderController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.css.*
import model.Cut
import org.w3c.dom.Element
import react.*
import react.dom.findDOMNode
import repository.ResearchRepository
import research.cut.CutContainer.CutContainerStyles.blackContainerStyle
import research.cut.CutContainer.CutContainerStyles.cutContainerStyle
import research.cut.CutContainer.CutContainerStyles.cutStyle
import research.cut.slider.SliderComponent
import research.cut.slider.sliderView
import styled.StyleSheet
import styled.css
import styled.styledDiv
import kotlin.browser.window

class CutContainer : RComponent<CutContainerProps, CutContainerState>() {

  private var testRef: Element? = null
  private val cutInput = PublishSubject<Input>()
  private val disposable = CompositeDisposable()

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
      delay(timeMillis = 100)
      testRef?.let {
        renderContent(it.clientHeight, it.clientWidth)
      }
    }
  }

  override fun RBuilder.render() {
    styledDiv {
      css(cutContainerStyle)
      styledDiv {
        css(blackContainerStyle)
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

  private fun renderContent(clientHeight: Int, clientWidth: Int) {
    react.dom.render(
      RBuilder().cut(
        dependencies = object : CutParentComponent.Dependencies,
          Dependencies by props.dependencies {
          override val cutsInput: Observable<Input> = this@CutContainer.cutInput
          override val height: Int = clientHeight
          override val width: Int = clientWidth
        }
      ).asElementOrNull(),
      container = testRef
    )
  }

  private fun sliderOutput(output: SliderController.Output) {
    when (output) {
      is SliderController.Output.SliceNumberChanged ->
        cutInput.onNext(Input.SliceNumberChanged(output.sliceNumber))
    }
  }

  override fun componentWillUnmount() = disposable.dispose()

  interface Dependencies {
    val storeFactory: StoreFactory
    val cut: Cut
    val cutsInput: Observable<Input>
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
