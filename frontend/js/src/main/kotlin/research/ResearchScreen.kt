package research

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.ccfraser.muirwik.components.spacingUnits
import components.loading
import controller.ResearchController
import controller.ResearchControllerImpl
import controller.ToolsController
import destroy
import kotlinx.css.*
import react.*
import repository.ResearchRepository
import research.ResearchScreen.ResearchStyles.appFrameContainerStyle
import research.ResearchScreen.ResearchStyles.mainContentContainerStyle
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
//  private val listInput: (Observer<CutsController.Input>) -> Disposable = ::listInput

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
    styledDiv {
      css(appFrameContainerStyle)

      val model = state.researchModel
      if (model.data == null) {
        loading(model.loading)
      } else {

        //leftDrawer
        toolsDrawer(
          open = state.toolsOpen,
          drawerWidth = drawerWidth,
          onOpen = ::openTools,
          onClose = ::closeTools
        ) {
          tools(dependencies = object : ToolsViewComponent.Dependencies,
            Dependencies by props.dependencies {
            override val toolsOutput: (ToolsController.Output) -> Unit = ::toolsOutput
          })
        }


        //mainContent
        styledDiv {
          css(mainContentContainerStyle)
          css {
            marginLeft = if (state.toolsOpen) drawerWidth.px else 7.spacingUnits
//            marginRight = if (state.rightDrawerOpen) 350.px else 7.spacingUnits
          }

          //TODO(тут остановился)

//            cuts(dependencies = object : CutsViewComponent.Dependencies,
//              Dependencies by props.dependencies {
//              override val cutsInput: (Observer<CutController.Input>) -> Disposable = this@App.cutsInput
//            })
          //
          //        val data = state.data
          //        if (data != null && gridModel != null) {
          //          styledDiv {
          //            css(columnOfRowsStyle)
          //            when (gridModel.type) {
          //              CutsGridType.SINGLE -> singleCutContainer(
          //                gridModel.cells[0],
          //                cellDoubleClickListener
          //              )
          //              CutsGridType.TWO_VERTICAL -> twoVerticalCutsContainer(
          //                gridModel.cells,
          //                cellDoubleClickListener
          //              )
          //              CutsGridType.TWO_HORIZONTAL -> twoHorizontalCutsContainer(
          //                gridModel.cells,
          //                cellDoubleClickListener
          //              )
          //              CutsGridType.THREE -> threeCutsContainer(
          //                gridModel.cells,
          //                cellDoubleClickListener
          //              )
          //              CutsGridType.FOUR -> TODO()//fourCutsContainer()
          //            }
          //          }
          //        }
//      rightDrawer()
//      ctTypes.firstOrNull { it.ctType == state.ctTypeToConfirm }?.let {
//        confirmationDialog(state.confirmationDialogOpen, it)
//      }
//      nonRelevantDialog()
        }
      }
    }
  }

//  private fun listInput(observer: Observer<TodoListController.Input>): Disposable {
//    listInputObserver = observer
//
//    return Disposable { listInputObserver = null }
//  }

  private fun toolsOutput(output: ToolsController.Output) {
    when (output) {
      is ToolsController.Output.ItemSelected -> TODO()
      is ToolsController.Output.BlackChanged -> TODO()
      is ToolsController.Output.WhiteChanged -> TODO()
      is ToolsController.Output.GammaChanged -> TODO()
      is ToolsController.Output.MipMethodChanged -> TODO()
      is ToolsController.Output.MipValueChanged -> TODO()
      is ToolsController.Output.PresetChanged -> TODO()
      is ToolsController.Output.Close -> researchViewDelegate.dispatch(ResearchView.Event.Close)
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
