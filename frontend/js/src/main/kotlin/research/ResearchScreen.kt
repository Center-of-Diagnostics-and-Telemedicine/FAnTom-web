package research

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.publish.PublishSubject
import com.ccfraser.muirwik.components.spacingUnits
import components.loading
import controller.*
import controller.ToolsController.Output
import destroy
import kotlinx.css.*
import model.ResearchSlicesSizesData
import react.*
import repository.MarksRepository
import repository.ResearchRepository
import research.ResearchScreen.ResearchStyles.appFrameContainerStyle
import research.gridcontainer.CutsContainerViewComponent
import research.gridcontainer.cuts
import research.marks.MarksComponent
import research.marks.marks
import research.tools.ToolsComponent
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

  private val cutsContainerInputObservable = PublishSubject<CutsContainerController.Input>()
  private val cutsInputObservable = PublishSubject<CutController.Input>()
  private val marksInputObservable = PublishSubject<MarksController.Input>()
  private val toolsInputObservable = PublishSubject<ToolsController.Input>()

//  private var gridContainerInputObserver: Observer<GridContainerController.Input>? = null
//  private val gridContainerInput: (Observer<GridContainerController.Input>) -> Disposable = ::gridContainerInput

//  private var cutsInputObserver: Observer<CutController.Input>? = null
//  private val cutsInput: (Observer<CutController.Input>) -> Disposable = ::cutsInput

  init {
    state = ResearchState(
      toolsOpen = false,
      marksOpen = false,
      researchModel = initialResearchModel()
    )
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
        leftDrawer(
          open = state.toolsOpen,
          drawerWidth = drawerWidth,
          onOpen = ::openTools,
          onClose = ::closeTools
        ) {
          tools(dependencies = object : ToolsComponent.Dependencies,
            Dependencies by props.dependencies {
            override val toolsOutput: (Output) -> Unit = ::toolsOutput
            override val toolsInput: Observable<ToolsController.Input> = this@ResearchScreen.toolsInputObservable
          })
        }

        mainContent(
          marginnLeft = if (state.toolsOpen) drawerWidth.px else 7.spacingUnits,
          marginnRight = if (state.marksOpen) drawerWidth.px else 7.spacingUnits
        ) {
          cuts(dependencies = object : CutsContainerViewComponent.Dependencies,
            Dependencies by props.dependencies {
            override val data: ResearchSlicesSizesData = model.data!!
            override val cutsContainerInputs: Observable<CutsContainerController.Input> = this@ResearchScreen.cutsContainerInputObservable
            override val cutsContainerOutput: (CutsContainerController.Output) -> Unit = ::gridOutput
            override val cutsInput: Observable<CutController.Input> = this@ResearchScreen.cutsInputObservable
          })
        }

        rightDrawer(
          open = state.marksOpen,
          drawerWidth = drawerWidth,
          onOpen = { },
          onClose = { }
        ) {
          marks(dependencies = object : MarksComponent.Dependencies,
            Dependencies by props.dependencies {
            override val marksOutput: (MarksController.Output) -> Unit = ::marksOutput
            override val marksInput: Observable<MarksController.Input> = this@ResearchScreen.marksInputObservable
          })
        }
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
        cutsContainerInputObservable.onNext(CutsContainerController.Input.GridChanged(output.grid))
      is Output.Close -> researchViewDelegate.dispatch(ResearchView.Event.Close)
    }
  }

  private fun gridOutput(output: CutsContainerController.Output) {
    when (output) {
      is CutsContainerController.Output.OpenFullCut -> TODO()
      is CutsContainerController.Output.CloseFullCut -> TODO()
      is CutsContainerController.Output.CircleDrawn -> marksInputObservable.onNext(
        MarksController.Input.AddNewMark(output.circle, output.sliceNumber, output.cut)
      )
      is CutsContainerController.Output.SelectMark -> marksInputObservable.onNext(
        MarksController.Input.SelectMark(output.mark)
      )
      is CutsContainerController.Output.UnselectMark -> marksInputObservable.onNext(
        MarksController.Input.UnselectMark(output.mark)
      )
      is CutsContainerController.Output.ContrastBrightnessChanged -> toolsInputObservable.onNext(
        ToolsController.Input.ContrastBrightnessChanged(output.black, output.white)
      )
      is CutsContainerController.Output.UpdateMark -> marksInputObservable.onNext(
        MarksController.Input.UpdateMark(output.markToUpdate)
      )
    }.let { }
  }

  private fun marksOutput(output: MarksController.Output) {
    when (output) {
      is MarksController.Output.Marks ->
        cutsInputObservable.onNext(CutController.Input.Marks(output.list))
    }
  }

  private fun updateState(model: ResearchView.Model) = setState { researchModel = model }
  private fun closeTools() = setState { toolsOpen = false }
  private fun openTools() = setState { toolsOpen = true }

  override fun componentWillUnmount() {
    lifecycleRegistry.destroy()
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

  interface Dependencies {
    val storeFactory: StoreFactory
    val researchRepository: ResearchRepository
    val marksRepository: MarksRepository
    val researchId: Int
  }

}

class ResearchState(
  var toolsOpen: Boolean,
  var marksOpen: Boolean,
  var researchModel: ResearchView.Model
) : RState

interface ResearchProps : RProps {
  var dependencies: ResearchScreen.Dependencies
}

fun RBuilder.research(dependencies: ResearchScreen.Dependencies) = child(ResearchScreen::class) {
  attrs.dependencies = dependencies
}
