package list

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.card.mCardActionArea
import com.ccfraser.muirwik.components.card.mCardContent
import com.ccfraser.muirwik.components.card.mCardHeader
import com.ccfraser.muirwik.components.styles.Breakpoint
import controller.ListController
import controller.ListControllerImpl
import kotlinx.css.*
import list.ListScreen.ListStyles.appFrameCss
import react.*
import repository.ResearchRepository
import root.LifecycleWrapper
import styled.StyleSheet
import styled.css
import styled.styledDiv
import view.FilterView
import view.ListView
import view.initialFilterModel
import view.initialListModel

class ListScreen(props: ListProps) :
  RComponent<ListProps, ListState>(props) {

  private val listViewDelegate = ListViewProxy(updateState = ::updateState)
  private val filtersViewDelegate = FiltersViewProxy(updateState = ::updateState)
  private val lifecycleWrapper = LifecycleWrapper()
  private lateinit var controller: ListController

  private val drawerWidth = 240

  init {
    state = ListState(
      drawerOpen = false,
      listModel = initialListModel(),
      filtersModel = initialFilterModel()
    )
  }

  private fun updateState(newModel: ListView.Model) {
    setState { listModel = newModel }
  }

  private fun updateState(newModel: FilterView.Model) {
    setState { filtersModel = newModel }
  }

  override fun componentDidMount() {
    lifecycleWrapper.start()
    controller = createController()
    controller.onViewCreated(
      listViewDelegate,
      filtersViewDelegate,
      lifecycleWrapper.lifecycle,
      props.dependencies.output
    )
  }

  private fun createController(): ListController {
    val dependencies = props.dependencies
    val todoListControllerDependencies =
      object : ListController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleWrapper.lifecycle
      }
    return ListControllerImpl(todoListControllerDependencies)
  }

  override fun RBuilder.render() {
    themeContext.Consumer { theme ->
      styledDiv {
        css {
          flexGrow = 1.0
          width = 100.pct
          zIndex = 0
          overflow = Overflow.hidden
          position = Position.relative
          display = Display.flex
          backgroundColor = Color(theme.palette.background.default)
        }

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
            filters(
              open = state.drawerOpen,
              drawerWidth = drawerWidth,
              filters = state.filtersModel.items
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
              css {
                padding(16.px)
                height = 100.pct
                width = 100.pct
                flexGrow = 1.0
                minWidth = 0.px
              }
              //mainContent
              mGridContainer(MGridSpacing.spacing2) {
                val breakpoints = MGridBreakpoints(MGridSize.cells6)
                  .up(Breakpoint.lg, MGridSize.cells4)
                  .down(Breakpoint.sm, MGridSize.cells12)
                val researches = state.listModel.items
                if (researches.isNotEmpty()) {
                  for (research in researches) {
                    mGridItem(breakpoints) {
                      mCardActionArea(onClick = {
                        listViewDelegate.dispatch(
                          ListView.Event.ItemClick(
                            research.name
                          )
                        )
                      }) {
                        mCard {
                          css {
                            if (research.seen) {
                              borderStyle = BorderStyle.solid
                              borderColor = Color.yellow
                            }
                            if (research.done) {
                              borderStyle = BorderStyle.solid
                              borderColor = Color.green
                            }
                          }
                          mCardHeader(title = research.name)
                          mCardContent {
                            mTypography(text = research.id.toString())
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

  }


  private fun closeDrawer() {
    setState { drawerOpen = false }
  }

  private fun openDrawer() {
    setState { drawerOpen = true }
  }

  object ListStyles : StyleSheet("ListScreenStyles", isStatic = true) {

    val appFrameCss by ListStyles.css {
      overflow = Overflow.hidden
      position = Position.relative
      display = Display.flex
      height = 100.pct
      width = 100.pct
    }

    val appBarSpacerCss by ListStyles.css {
      display = Display.flex
      alignItems = Align.center
      justifyContent = JustifyContent.flexEnd
      height = 64.px
    }
  }

  interface Dependencies {
    val output: (ListController.Output) -> Unit
    val storeFactory: StoreFactory
    val researchRepository: ResearchRepository
  }
}

class ListState(
  var listModel: ListView.Model,
  var filtersModel: FilterView.Model,
  var drawerOpen: Boolean = false
) : RState


interface ListProps : RProps {
  var dependencies: ListScreen.Dependencies
}

fun RBuilder.list(dependencies: ListScreen.Dependencies) = child(ListScreen::class) {
  attrs.dependencies = dependencies
}
