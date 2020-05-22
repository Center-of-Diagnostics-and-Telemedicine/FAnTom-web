package list

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.card.mCardActionArea
import com.ccfraser.muirwik.components.card.mCardContent
import com.ccfraser.muirwik.components.card.mCardHeader
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItemWithIcon
import com.ccfraser.muirwik.components.styles.Breakpoint
import controller.ListController
import controller.ListControllerImpl
import kotlinext.js.jsObject
import kotlinx.css.*
import kotlinx.html.role
import react.*
import react.dom.div
import react.dom.jsStyle
import repository.ResearchRepository
import root.LifecycleWrapper
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
      slideOutDrawerOpen = false,
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
          css {
            overflow = Overflow.hidden
            position = Position.relative
            display = Display.flex
            height = 100.pct
            width = 100.pct
          }

          mAppBar(position = MAppBarPosition.absolute) {
            attrs.asDynamic().style = kotlinext.js.js {
              zIndex = theme.zIndex.drawer + 1
            }
            mToolbar(disableGutters = !state.slideOutDrawerOpen) {
              if (!state.slideOutDrawerOpen) {
                mIconButton(
                  "menu",
                  color = MColor.inherit,
                  onClick = { setState { slideOutDrawerOpen = true } })
              }
              mToolbarTitle("Fantom")
            }
          }

          val pp: MPaperProps = jsObject { }
          pp.asDynamic().style = kotlinext.js.js {
            position = "relative"
            transition = "width 195ms cubic-bezier(0.4, 0, 0.2, 1) 0ms"
            height = "100%"
            minHeight = "100vh"
            if (!state.slideOutDrawerOpen) {
              overflowX = "hidden"
              width = 7.spacingUnits.value
            } else {
              width = drawerWidth + 1
            }
          }
          mDrawer(
            state.slideOutDrawerOpen,
            MDrawerAnchor.left,
            MDrawerVariant.permanent,
            paperProps = pp
          ) {
            attrs.onMouseEnter = { setState { slideOutDrawerOpen = true } }
            attrs.onMouseLeave = { setState { slideOutDrawerOpen = false } }
            div {
              attrs.jsStyle = kotlinext.js.js {
                display = "flex"; alignItems = "center"; justifyContent = "flex-end"; height = 64
              }
            }
            mDivider()
            div {
              attrs {
                role = "button"
              }
            }
            mList {
              css {
                backgroundColor = Color(theme.palette.background.paper)
                width = if (state.slideOutDrawerOpen) LinearDimension.auto else drawerWidth.px
              }
              mListItemWithIcon("search", "Просмотренные", divider = false)
              mListItemWithIcon("done", "Оконченные", divider = false)
            }
          }

          div {
            attrs.jsStyle = kotlinext.js.js {
              flexGrow = 1
            }
            styledDiv {
              css {
                toolbarJsCssToPartialCss(theme.mixins.toolbar)
              }
            }
            mDivider { }
            styledDiv {
              css {
                padding(16.px)
                height = 100.pct
                width = 100.pct
                flexGrow = 1.0
                minWidth = 0.px
              }
              mGridContainer(MGridSpacing.spacing2) {
                val breakpoints = MGridBreakpoints(MGridSize.cells6)
                  .up(Breakpoint.lg, MGridSize.cells4)
                  .down(Breakpoint.sm, MGridSize.cells12)
                val researches = state.listModel.items
                if (researches.isNotEmpty()) {
                  for (research in researches) {
                    mGridItem(breakpoints) {
                      mCardActionArea(onClick = {
                        listViewDelegate.dispatchEvent(
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

  interface Dependencies {
    val output: (ListController.Output) -> Unit
    val storeFactory: StoreFactory
    val researchRepository: ResearchRepository
  }
}

class ListState(
  var listModel: ListView.Model,
  var filtersModel: FilterView.Model,
  var slideOutDrawerOpen: Boolean = false
) : RState


interface ListProps : RProps {
  var dependencies: ListScreen.Dependencies
}

fun RBuilder.list(dependencies: ListScreen.Dependencies) = child(ListScreen::class) {
  attrs.dependencies = dependencies
}