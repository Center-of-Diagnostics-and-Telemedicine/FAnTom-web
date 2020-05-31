package research

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.publish.PublishSubject
import com.ccfraser.muirwik.components.spacingUnits
import components.loading
import controller.CutController
import controller.GridContainerController
import controller.ResearchController
import controller.ResearchControllerImpl
import controller.ToolsController.Output
import destroy
import kotlinx.css.*
import model.ResearchSlicesSizesData
import react.*
import repository.ResearchRepository
import research.ResearchScreen.ResearchStyles.appFrameContainerStyle
import research.gridcontainer.GridContainerViewComponent
import research.gridcontainer.cuts
import research.tools.ToolsViewComponent
import research.tools.tools
import resume
import styled.StyleSheet
import styled.css
import styled.styledDiv
import view.ResearchView
import view.initialResearchModel

class ResearchScreen(prps: ResearchProps) : RComponent<ResearchProps, ResearchState>(prps) {

  private val drawerWidth = 300

  private val researchViewDelegate = ResearchViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: ResearchController

  private val gridContainerInputObservable = PublishSubject<GridContainerController.Input>()
  private val cutsInputObservable = PublishSubject<CutController.Input>()

//  private var gridContainerInputObserver: Observer<GridContainerController.Input>? = null
//  private val gridContainerInput: (Observer<GridContainerController.Input>) -> Disposable = ::gridContainerInput

//  private var cutsInputObserver: Observer<CutController.Input>? = null
//  private val cutsInput: (Observer<CutController.Input>) -> Disposable = ::cutsInput

  init {
    state = ResearchState(false, initialResearchModel())
  }

  override fun componentDidMount() {
    lifecycleRegistry.resume()
    controller = createController()
    controller.onViewCreated(researchViewDelegate, lifecycleRegistry)
  }

  private fun createController(): ResearchController {
    val dependencies = props.dependencies
    val researchControllerDependencies =
      object : ResearchController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
      }
    return ResearchControllerImpl(researchControllerDependencies)
  }

  override fun RBuilder.render() {
    val model = state.researchModel
    loading(model.loading)
    styledDiv {
      css(appFrameContainerStyle)


      if (model.data != null) {
        //leftDrawer
        toolsDrawer(
          open = state.toolsOpen,
          drawerWidth = drawerWidth,
          onOpen = ::openTools,
          onClose = ::closeTools
        ) {
          tools(dependencies = object : ToolsViewComponent.Dependencies,
            Dependencies by props.dependencies {
            override val toolsOutput: (Output) -> Unit = ::toolsOutput
          })
        }

        mainContent(
          margiLeft = if (state.toolsOpen) drawerWidth.px else 7.spacingUnits
        ) {
          cuts(dependencies = object : GridContainerViewComponent.Dependencies,
            Dependencies by props.dependencies {
            override val data: ResearchSlicesSizesData = model.data!!
            override val gridContainerInputs: Observable<GridContainerController.Input> = this@ResearchScreen.gridContainerInputObservable
            override val cutsInput: Observable<CutController.Input> = this@ResearchScreen.cutsInputObservable
          })
        }
//      rightDrawer()
//      ctTypes.firstOrNull { it.ctType == state.ctTypeToConfirm }?.let {
//        confirmationDialog(state.confirmationDialogOpen, it)
//      }
//      nonRelevantDialog()
      }
    }
  }

  private fun toolsOutput(output: Output) {
    when (output) {
      is Output.BlackChanged -> cutsInputObservable.onNext(
        CutController.Input.BlackChanged(output.value)
      )
      is Output.WhiteChanged -> cutsInputObservable.onNext(
        CutController.Input.WhiteChanged(output.value)
      )
      is Output.GammaChanged -> cutsInputObservable.onNext(
        CutController.Input.GammaChanged(output.value)
      )
      is Output.MipMethodChanged ->
        cutsInputObservable.onNext(CutController.Input.MipMethodChanged(output.mip))
      is Output.MipValueChanged ->
        cutsInputObservable.onNext(CutController.Input.MipValueChanged(output.value))
      is Output.PresetChanged ->
        cutsInputObservable.onNext(CutController.Input.PresetChanged(output.preset))
      is Output.GridChanged ->
        gridContainerInputObservable.onNext(GridContainerController.Input.GridChanged(output.grid))
      is Output.Close -> researchViewDelegate.dispatch(ResearchView.Event.Close)
    }
  }

  private fun updateState(model: ResearchView.Model) = setState { researchModel = model }
  private fun closeTools() = setState { toolsOpen = false }
  private fun openTools() = setState { toolsOpen = true }

  override fun componentWillUnmount() {
    lifecycleRegistry.destroy()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val researchRepository: ResearchRepository
    val researchId: Int
  }

  object ResearchStyles : StyleSheet("ResearchStyles", isStatic = true) {

    val appFrameContainerStyle by css {
      flex(1.0)
      display = Display.flex
      flexDirection = FlexDirection.row
    }

    val mainContentContainerStyle by css {
      flex(1.0)
      display = Display.flex
      flexDirection = FlexDirection.row
      height = 100.pct
      minHeight = 100.vh
    }

  }

}

class ResearchState(
  var toolsOpen: Boolean,
  var researchModel: ResearchView.Model
) : RState

interface ResearchProps : RProps {
  var dependencies: ResearchScreen.Dependencies
}

fun RBuilder.research(dependencies: ResearchScreen.Dependencies) = child(ResearchScreen::class) {
  attrs.dependencies = dependencies
}
