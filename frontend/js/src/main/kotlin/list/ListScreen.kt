package list

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.ccfraser.muirwik.components.mCssBaseline
import com.ccfraser.muirwik.components.themeContext
import com.ccfraser.muirwik.components.toolbarJsCssToPartialCss
import controller.ListController
import controller.ListControllerImpl
import destroy
import kotlinx.css.*
import list.ListScreen.ListStyles.appFrameCss
import list.ListScreen.ListStyles.mainContainerCss
import list.ListScreen.ListStyles.screenContainerCss
import model.Category
import model.Filter
import model.Research
import react.*
import repository.ResearchRepository
import resume
import styled.StyleSheet
import styled.css
import styled.styledDiv
import view.*

class ListScreen(props: ListProps) :
  RComponent<ListProps, ListState>(props) {

  private val listViewDelegate = ListViewProxy(updateState = ::updateState)
  private val filtersViewDelegate = FiltersViewProxy(updateState = ::updateState)
  private val categoryViewDelegate = CategoriesViewProxy(updateState = ::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: ListController

  private val drawerWidth = 240

  init {
    state = ListState(
      drawerOpen = false,
      listModel = initialListModel(),
      filtersModel = initialFilterModel(),
      categoriesModel = initialCategoryModel()
    )
  }

  override fun componentDidMount() {
    lifecycleRegistry.resume()
    controller = createController()
    controller.onViewCreated(
      listViewDelegate,
      filtersViewDelegate,
      categoryViewDelegate,
      lifecycleRegistry
    )
  }

  private fun createController(): ListController {
    val dependencies = props.dependencies
    val listControllerDependencies =
      object : ListController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
      }
    return ListControllerImpl(listControllerDependencies)
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

              categories(
                categories = state.categoriesModel.items,
                onClick = ::onCategoryClick,
                currentCategory = state.categoriesModel.current
              )

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

  private fun onListItemClick(research: Research) {
    listViewDelegate.dispatch(ListView.Event.ItemClick(research))
  }

  private fun onCategoryClick(category: Category) {
    categoryViewDelegate.dispatch(CategoryView.Event.ItemClick(category))
  }

  private fun closeDrawer() = setState { drawerOpen = false }
  private fun openDrawer() = setState { drawerOpen = true }

  private fun updateState(newModel: ListView.Model) = setState { listModel = newModel }
  private fun updateState(newModel: FilterView.Model) = setState { filtersModel = newModel }
  private fun updateState(newModel: CategoryView.Model) = setState { categoriesModel = newModel }

  override fun componentWillUnmount() {
    lifecycleRegistry.destroy()
  }

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

    val categoriesContainerCss by ListStyles.css {
      display = Display.flex
      justifyContent = JustifyContent.start
      flexWrap = FlexWrap.wrap
      marginBottom = 16.px
    }

    val chipMarginCss by ListStyles.css {
      margin(4.px)
    }
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val researchRepository: ResearchRepository
    val listOutput: (ListController.Output) -> Unit
  }
}

class ListState(
  var listModel: ListView.Model,
  var filtersModel: FilterView.Model,
  var categoriesModel: CategoryView.Model,
  var drawerOpen: Boolean = false
) : RState


interface ListProps : RProps {
  var dependencies: ListScreen.Dependencies
}

fun RBuilder.list(dependencies: ListScreen.Dependencies) = child(ListScreen::class) {
  attrs.dependencies = dependencies
}
