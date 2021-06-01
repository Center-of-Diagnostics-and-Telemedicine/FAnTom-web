package decompose.research

import com.arkivanov.decompose.RouterState
import com.ccfraser.muirwik.components.mCssBaseline
import com.ccfraser.muirwik.components.spacingUnits
import com.ccfraser.muirwik.components.themeContext
import components.research.ResearchRoot
import components.research.ResearchRoot.*
import components.screenLoading
import decompose.RenderableComponent
import decompose.renderableChild
import decompose.research.ResearchRootUi.State
import decompose.research.marks.MarksUi
import decompose.research.tools.ToolsUi
import kotlinx.css.LinearDimension
import kotlinx.css.px
import react.RBuilder
import react.RState
import react.setState
import research.*
import styled.css
import styled.styledDiv

class ResearchRootUi(props: Props<ResearchRoot>) : RenderableComponent<ResearchRoot, State>(
  props = props,
  initialState = State(
    model = props.component.models.value,
    toolsRouterState = props.component.toolsRouterState.value,
    marksRouterState = props.component.marksRouterState.value,
    toolsOpen = true,
    marksOpen = true
  )
) {

  private val drawerWidth = 240
  private val drawerBigMargin = 16.spacingUnits
  private val drawerLittleMargin = 8.spacingUnits

  init {
    component.models.bindToState { model = it }
    component.toolsRouterState.bindToState { toolsRouterState = it }
    component.marksRouterState.bindToState { marksRouterState = it }
  }

  override fun RBuilder.render() {
    mCssBaseline()

    themeContext.Consumer { theme ->
      screenLoading(state.model.loading)

      styledDiv {
        css(ResearchScreen.ResearchStyles.appFrameContainerStyle)
//        if (state.model.data != null) {
        leftMenu()
        //          contentWithCuts(state.model)
//        rightMenu(drawerLittleMargin) {
//          renderableChild(MarksUi::class, state.marksRouterState.activeChild.instance.component)
//        }
//          when (category) {
//            Category.Covid -> {
//              rightMenu(drawerBigMargin) {
//                covidMarks(dependencies = covidMarksDependencies(state.model))
//              }
//            }
//            Category.Expert -> {
//              rightMenu(drawerLittleMargin) {
//                expertMarks(dependencies = expertMarksDependencies(state.model))
//              }
//            }
//            Category.DoseReport -> {
//              rightMenu(drawerLittleMargin) {
//                marks(dependencies = marksDependencies(state.model.data))
//                doseReportMarks(dependencies = doseReportMarksDependencies(state.model))
//              }
//            }
//            else -> {
//              rightMenu(drawerLittleMargin) {
//                marks(dependencies = marksDependencies(state.model.data))
//              }
//            }
//          }
//        }

      }

    }
  }

  private fun RBuilder.leftMenu() {
    leftDrawer(
      open = state.toolsOpen,
      drawerWidth = if (state.toolsOpen) drawerWidth.px else drawerLittleMargin,
      onOpen = ::openTools,
      onClose = ::closeTools
    ) {
      leftDrawerHeaderButton(
        open = state.toolsOpen,
        onClick = { setState { toolsOpen = !state.toolsOpen } }
      )
      renderableChild(ToolsUi::class, state.toolsRouterState.activeChild.instance.component)
    }
  }

  private fun RBuilder.rightMenu(
    drawerMargin: LinearDimension,
    block: RBuilder.() -> Unit
  ) {
    rightDrawer(
      open = state.marksOpen,
      drawerWidth = if (state.marksOpen) drawerWidth.px else drawerMargin,
      onOpen = ::openMarks,
      onClose = ::closeMarks
    ) {
      rightDrawerHeaderButton(
        open = state.marksOpen,
        onClick = { setState { marksOpen = !state.marksOpen } }
      )
      block()
    }
  }

  private fun closeTools() = setState { toolsOpen = false }
  private fun openTools() = setState { toolsOpen = true }
  private fun closeMarks() = setState { marksOpen = false }
  private fun openMarks() = setState { marksOpen = true }

  class State(
    var model: Model,
    var toolsRouterState: RouterState<*, Tools>,
    var marksRouterState: RouterState<*, Marks>,
    var toolsOpen: Boolean,
    var marksOpen: Boolean,
  ) : RState
}