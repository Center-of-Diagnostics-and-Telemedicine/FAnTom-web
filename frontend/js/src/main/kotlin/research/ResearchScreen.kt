package research

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.ccfraser.muirwik.components.spacingUnits
import controller.ToolsController
import kotlinx.css.*
import react.*
import research.ResearchScreen.ResearchStyles.appFrameContainerStyle
import research.ResearchScreen.ResearchStyles.mainContentContainerStyle
import research.tools.ToolsViewComponent
import research.tools.tools
import styled.StyleSheet
import styled.css
import styled.styledDiv

class ResearchScreen(prps: ResearchProps) : RComponent<ResearchProps, ResearchState>(prps) {

  private val drawerWidth = 300
  //left menu
  //rigth menu
  //center content

  init {
    state = ResearchState(false)
  }

  override fun RBuilder.render() {
    styledDiv {
      css(appFrameContainerStyle)

      //leftDrawer
      toolsDrawer(
        open = state.toolsOpen,
        drawerWidth = drawerWidth,
        onOpen = ::openTools,
        onClose = ::closeTools
      ) {
        tools(dependencies = object : ToolsViewComponent.Dependencies,
          Dependencies by props.dependencies {
          override val output: (ToolsController.Output) -> Unit
            get() = TODO("Not yet implemented")

        })
      }

      //mainContent
      styledDiv {
        css(mainContentContainerStyle)
        css {
          marginLeft = if (state.toolsOpen) drawerWidth.px else 7.spacingUnits
//            marginRight = if (state.rightDrawerOpen) 350.px else 7.spacingUnits
        }
//        loading(state.loading)
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
      }
//      rightDrawer()
//      ctTypes.firstOrNull { it.ctType == state.ctTypeToConfirm }?.let {
//        confirmationDialog(state.confirmationDialogOpen, it)
//      }
//      nonRelevantDialog()
    }


  }

  private fun closeTools() = setState { toolsOpen = false }
  private fun openTools() = setState { toolsOpen = true }

  interface Dependencies {
    val storeFactory: StoreFactory
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
  var toolsOpen: Boolean
) : RState

interface ResearchProps : RProps {
  var dependencies: ResearchScreen.Dependencies
}

fun RBuilder.research(dependencies: ResearchScreen.Dependencies) = child(ResearchScreen::class) {
  attrs.dependencies = dependencies
}
