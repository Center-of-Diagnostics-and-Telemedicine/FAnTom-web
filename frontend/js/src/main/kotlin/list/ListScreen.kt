package list

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.ccfraser.muirwik.components.themeContext
import com.ccfraser.muirwik.components.toolbarJsCssToPartialCss
import controller.ListController
import controller.ListControllerImpl
import kotlinx.css.*
import list.ListScreen.ListStyles.appFrameCss
import list.ListScreen.ListStyles.mainContainerCss
import list.ListScreen.ListStyles.screenContainerCss
import model.Filter
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
            filters(
              open = state.drawerOpen,
              drawerWidth = drawerWidth,
              filters = state.filtersModel.items,
              currentFilter = state.filtersModel.current,
              onClick = ::onFilterClick
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
              researchList(
                items = state.listModel.items,
                onClick = ::onListItemClick
              )
            }
          }
        }
      }
    }

  }

  private fun onFilterClick(filter: Filter) {
    filtersViewDelegate.dispatch(FilterView.Event.ItemClick(filter))
  }

  private fun onListItemClick(id: Int) {
    listViewDelegate.dispatch(ListView.Event.ItemClick(id))
  }

  private fun closeDrawer() = setState { drawerOpen = false }
  private fun openDrawer() = setState { drawerOpen = true }

  private fun updateState(newModel: ListView.Model) = setState { listModel = newModel }
  private fun updateState(newModel: FilterView.Model) = setState { filtersModel = newModel }

  object ListStyles : StyleSheet("ListScreenStyles", isStatic = true) {

    val screenContainerCss by ListStyles.css {
      flexGrow = 1.0
      width = 100.pct
      zIndex = 0
      overflow = Overflow.hidden
      position = Position.relative
      display = Display.flex
    }

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

    val mainContainerCss by ListStyles.css {
      padding(16.px)
      height = 100.pct
      width = 100.pct
      flexGrow = 1.0
      minWidth = 0.px
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
