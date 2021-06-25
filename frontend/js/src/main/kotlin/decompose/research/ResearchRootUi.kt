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
import decompose.research.cuts.CutsContainerUi
import decompose.research.marks.MarksUi
import decompose.research.tools.ToolsUi
import kotlinx.css.*
import react.RBuilder
import react.RState
import react.setState
import research.*
import styled.css
import styled.styledDiv
import decompose.Props

class ResearchRootUi(props: Props<ResearchRoot>) : RenderableComponent<ResearchRoot, State>(
  props = props,
  initialState = State(
    model = props.component.models.value,
    toolsRouterState = props.component.toolsRouterState.value,
    marksRouterState = props.component.marksRouterState.value,
    cutsContainerRouterState = props.component.cutsContainerRouterState.value,
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
    component.cutsContainerRouterState.bindToState { cutsContainerRouterState = it }
  }

  override fun RBuilder.render() {
    mCssBaseline()

    themeContext.Consumer { theme ->
      screenLoading(state.model.loading)

      styledDiv {
        css(ResearchScreen.ResearchStyles.appFrameContainerStyle)

        leftMenu {
          when (val toolsInstance = state.toolsRouterState.activeChild.instance) {
            is ToolsChild.Data -> renderableChild(ToolsUi::class, toolsInstance.component)
          }
        }

        contentWithCuts()

        rightMenu {
          when (val marksInstance = state.marksRouterState.activeChild.instance) {
            is MarksChild.Data -> renderableChild(MarksUi::class, marksInstance.component)
          }
        }

      }
    }
  }

  private fun RBuilder.contentWithCuts() {
    styledDiv {
      css {
        display = Display.flex
        height = 100.pct
        minHeight = 100.vh
        width = 100.pct
      }

      when (val cutsInstance = state.cutsContainerRouterState.activeChild.instance) {
        is CutsChild.Data -> renderableChild(CutsContainerUi::class, cutsInstance.component)
      }
    }
  }

  private fun RBuilder.leftMenu(
    block: RBuilder.() -> Unit
  ) {
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
      block()
    }
  }

  private fun RBuilder.rightMenu(
    block: RBuilder.() -> Unit
  ) {
    rightDrawer(
      open = state.marksOpen,
      drawerWidth = if (state.marksOpen) drawerWidth.px else drawerLittleMargin,
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
    var toolsRouterState: RouterState<*, ToolsChild>,
    var marksRouterState: RouterState<*, MarksChild>,
    var cutsContainerRouterState: RouterState<*, CutsChild>,
    var toolsOpen: Boolean,
    var marksOpen: Boolean,
  ) : RState
}