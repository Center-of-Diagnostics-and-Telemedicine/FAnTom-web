package list

import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItemWithIcon
import com.ccfraser.muirwik.components.themeContext
import kotlinx.css.*
import model.Filter
import react.RBuilder
import root.debugLog
import styled.css

fun RBuilder.filters(
  open: Boolean,
  drawerWidth: Int,
  filters: List<Filter>,
  onClick: (Filter) -> Unit,
  currentFilter: Filter
) {
  themeContext.Consumer { theme ->
    mList {
      css {
        backgroundColor = Color(theme.palette.background.paper)
        width = if (open) LinearDimension.auto else drawerWidth.px
      }
      debugLog("filters size = ${filters.size}")
      filters.forEach { filter ->
        mListItemWithIcon(
          iconName = filter.icon,
          primaryText = filter.name,
          divider = false,
          selected = currentFilter == filter,
          onClick = { onClick(filter) }
        )
      }
    }
  }
}
