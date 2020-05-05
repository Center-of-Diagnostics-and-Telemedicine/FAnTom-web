package presentation.screen

import client.newmvi.researchlist.view.ResearchListView
import com.badoo.reaktive.subject.publish.PublishSubject
import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.card.*
import com.ccfraser.muirwik.components.styles.Breakpoint
import debugLog
import kotlinext.js.jsObject
import kotlinx.css.*
import model.Research
import presentation.di.injectResearchList
import presentation.screen.viewcomponents.listMenu
import presentation.screen.viewcomponents.spacer
import react.*
import react.dom.div
import react.dom.jsStyle
import styled.css
import styled.styledDiv

class ResearchListScreen(props: ResearchListProps) :
  RComponent<ResearchListProps, ResearchListState>(props), ResearchListView {

  init {
    state = ResearchListState(slideOutDrawerOpen = false)
  }

  private val binder = injectResearchList()
  override val events: PublishSubject<ResearchListView.Event> = PublishSubject()
  private val drawerWidth = 240


  override fun show(model: ResearchListView.ResearchListViewModel) {
    setState {
      loading = model.isLoading
      error = model.error
      researches = model.data
    }
  }

  override fun componentDidMount() {
    binder.attachView(this@ResearchListScreen)
    binder.onStart()
    dispatch(ResearchListView.Event.Init)
  }

  override fun RBuilder.render() {
    debugLog("new state income, slideOut = ${state.slideOutDrawerOpen}")
    mCssBaseline()
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
              attrs.jsStyle = kotlinext.js.js { display = "flex"; alignItems = "center"; justifyContent = "flex-end"; height = 64 }
            }
            mDivider()
            listMenu(state.slideOutDrawerOpen, drawerWidth)
          }

          div {
            attrs.jsStyle = kotlinext.js.js {
              flexGrow = 1
            }
            spacer()
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
                val researches = state.researches
                if (researches.isNotEmpty()) {
                  for (research in researches) {
                    mGridItem(breakpoints) {
                      mCardActionArea(onClick = { props.onResearchSelected(research.id) }) {
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
                          mCardHeader(
                            title = research.name
                          )
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

  override fun componentWillUnmount() {
    binder.detachView()
    binder.onStop()
  }

  override fun dispatch(event: ResearchListView.Event) {
    events.onNext(event)
  }


}

class ResearchListState(
  var loading: Boolean = true,
  var error: String = "",
  var researches: List<Research> = listOf(),
  var slideOutDrawerOpen: Boolean = false
) : RState


interface ResearchListProps : RProps {
  var onResearchSelected: (Int) -> Unit
}

fun RBuilder.researchList(onResearcheSelected: (Int) -> Unit) = child(ResearchListScreen::class) {
  attrs.onResearchSelected = onResearcheSelected
}