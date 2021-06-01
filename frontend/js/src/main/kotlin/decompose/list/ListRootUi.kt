package decompose.list

import com.arkivanov.decompose.RouterState
import com.ccfraser.muirwik.components.*
import components.listroot.ListRoot
import decompose.RenderableComponent
import decompose.list.ListRootUi.ListStyles.appFrameCss
import decompose.list.ListRootUi.ListStyles.mainContainerCss
import decompose.list.ListRootUi.ListStyles.screenContainerCss
import decompose.list.ListRootUi.State
import decompose.list.filters.ListFiltersUi
import decompose.list.list.ListResearchUi
import decompose.renderableChild
import kotlinx.css.*
import list.*
import react.RBuilder
import react.RState
import react.setState
import styled.StyleSheet
import styled.css
import styled.styledDiv

class ListRootUi(props: Props<ListRoot>) : RenderableComponent<ListRoot, State>(
  props = props,
  initialState = State(
    drawerOpen = false,
    filterRouterState = props.component.filtersRouterState.value,
    listRouterState = props.component.listRouterState.value
  )
) {

  private val drawerWidth = 240

  init {
    component.filtersRouterState.bindToState { filterRouterState = it }
    component.listRouterState.bindToState { listRouterState = it }
  }

  override fun RBuilder.render() {
    mCssBaseline()

    themeContext.Consumer { theme ->
      styledDiv {
        css(screenContainerCss)
        css { backgroundColor = Color(theme.palette.background.default) }

        styledDiv {
          // App Frame
          css(appFrameCss)

          appBar(onClick = ::openDrawer)

          drawer(
            open = state.drawerOpen,
            openDrawer = ::openDrawer,
            closeDrawer = ::closeDrawer,
            drawerWidth = drawerWidth
          ) {
            renderableChild(
              ListFiltersUi::class,
              state.filterRouterState.activeChild.instance.component
            )
          }

          styledDiv {
            css { flexGrow = 1.0 }

            //toolbarSpacer
            styledDiv {
              css { toolbarJsCssToPartialCss(theme.mixins.toolbar) }
            }
            //mainContainer
            styledDiv {
              css(mainContainerCss)

              //mainContent
              renderableChild(
                ListResearchUi::class,
                state.listRouterState.activeChild.instance.component
              )
            }
          }
        }
      }
    }
  }

  private fun closeDrawer() = setState { drawerOpen = false }
  private fun openDrawer() = setState { drawerOpen = true }

  object ListStyles : StyleSheet("ListScreenStyles", isStatic = true) {

    val screenContainerCss by css {
      flexGrow = 1.0
      width = 100.pct
      zIndex = 0
      overflow = Overflow.hidden
      position = Position.relative
      display = Display.flex
    }

    val appFrameCss by css {
      overflow = Overflow.hidden
      position = Position.relative
      display = Display.flex
      height = 100.pct
      width = 100.pct
    }

    val appBarSpacerCss by css {
      display = Display.flex
      alignItems = Align.center
      justifyContent = JustifyContent.flexEnd
      height = 64.px
    }

    val mainContainerCss by css {
      padding(16.px)
      height = 100.pct
      width = 100.pct
      flexGrow = 1.0
      minWidth = 0.px
    }

    val categoriesContainerCss by css {
      display = Display.flex
      justifyContent = JustifyContent.start
      flexWrap = FlexWrap.wrap
      marginBottom = 16.px
    }

    val chipMarginCss by css {
      margin(4.px)
    }
  }


  class State(
    var filterRouterState: RouterState<*, ListRoot.Filters>,
    var listRouterState: RouterState<*, ListRoot.List>,
    var drawerOpen: Boolean
  ) : RState
}