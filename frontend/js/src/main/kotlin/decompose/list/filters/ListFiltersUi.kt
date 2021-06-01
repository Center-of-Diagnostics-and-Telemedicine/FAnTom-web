package decompose.list.filters

import com.ccfraser.muirwik.components.mCssBaseline
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.themeContext
import components.listfilters.ListFilters
import decompose.RenderableComponent
import decompose.list.filters.ListFiltersUi.State
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import kotlinx.css.*
import react.RBuilder
import react.RState
import styled.css
import styled.styledDiv

class ListFiltersUi(props: Props<ListFilters>) :
  RenderableComponent<ListFilters, State>(
    props = props,
    initialState = State(model = props.component.models.value)
  ) {

  init {
    component.models.bindToState { model = it }
  }

  override fun RBuilder.render() {
    styledDiv {
      mTypography { +"this is filter list ui" }
    }
  }

//  private fun onFilterClick(filter: Filter) {
//    filtersViewDelegate.dispatch(FilterView.Event.ItemClick(filter))
//  }
//
//  private fun onListItemClick(research: Research) {
//    listViewDelegate.dispatch(ListView.Event.ItemClick(research))
//  }
//
//  private fun onCategoryClick(category: Category) {
//    categoryViewDelegate.dispatch(CategoryView.Event.ItemClick(category))
//  }
//
//  private fun closeDrawer() = setState { drawerOpen = false }
//  private fun openDrawer() = setState { drawerOpen = true }

//  object ListStyles : StyleSheet("ListScreenStyles", isStatic = true) {
//
//    val screenContainerCss by css {
//      flexGrow = 1.0
//      width = 100.pct
//      zIndex = 0
//      overflow = Overflow.hidden
//      position = Position.relative
//      display = Display.flex
//    }
//
//    val appFrameCss by css {
//      overflow = Overflow.hidden
//      position = Position.relative
//      display = Display.flex
//      height = 100.pct
//      width = 100.pct
//    }
//
//    val appBarSpacerCss by css {
//      display = Display.flex
//      alignItems = Align.center
//      justifyContent = JustifyContent.flexEnd
//      height = 64.px
//    }
//
//    val mainContainerCss by css {
//      padding(16.px)
//      height = 100.pct
//      width = 100.pct
//      flexGrow = 1.0
//      minWidth = 0.px
//    }
//
//    val categoriesContainerCss by css {
//      display = Display.flex
//      justifyContent = JustifyContent.start
//      flexWrap = FlexWrap.wrap
//      marginBottom = 16.px
//    }
//
//    val chipMarginCss by css {
//      margin(4.px)
//    }
//  }

  class State(
    var model: ListFilters.Model,
  ) : RState
}