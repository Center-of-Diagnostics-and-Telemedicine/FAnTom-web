package decompose.list.filters


import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItemWithIcon
import com.ccfraser.muirwik.components.themeContext
import components.listfilters.ListFilters
import decompose.RenderableComponent
import decompose.list.filters.ListFiltersUi.State
import kotlinx.css.*
import react.RBuilder
import react.RState
import styled.css

class ListFiltersUi(props: Props<ListFilters>) :
  RenderableComponent<ListFilters, State>(
    props = props,
    initialState = State(model = props.component.models.value)
  ) {

  init {
    component.models.bindToState { model = it }
  }

  override fun RBuilder.render() {
    themeContext.Consumer { theme ->
      mList {
        css {
          backgroundColor = Color(theme.palette.background.paper)
        }
        state.model.items.forEach { filter ->
          mListItemWithIcon(
            iconName = filter.icon,
            primaryText = filter.name,
            divider = false,
            selected = state.model.current == filter,
            onClick = { component.onItemClick(filter) }
          )
        }
      }
    }

  }

  class State(
    var model: ListFilters.Model,
  ) : RState
}