package list

import com.ccfraser.muirwik.components.*
import kotlinext.js.jsObject
import react.RBuilder
import styled.css
import styled.styledDiv

fun RBuilder.drawer(
  open: Boolean,
  openDrawer: () -> Unit,
  closeDrawer: () -> Unit,
  drawerWidth: Int,
  block: RBuilder.() -> Unit
) {
  mDrawer(
    open,
    MDrawerAnchor.left,
    MDrawerVariant.permanent,
    paperProps = leftDrawerProps(open, drawerWidth = drawerWidth)
  ) {
    attrs.onMouseEnter = { openDrawer() }
    attrs.onMouseLeave = { closeDrawer() }
    appBarSpacer()
    block()
  }
}

fun RBuilder.appBarSpacer() {
  styledDiv {
    css(ListScreen.ListStyles.appBarSpacerCss)
  }
}

private fun leftDrawerProps(open: Boolean, drawerWidth: Int): MPaperProps {
  val pp: MPaperProps = jsObject { }
  pp.asDynamic().style = kotlinext.js.js {
    position = "relative"
    transition = "width 195ms cubic-bezier(0.4, 0, 0.2, 1) 0ms"
    height = "100%"
    minHeight = "100vh"
    if (open.not()) {
      overflowX = "hidden"
      width = 7.spacingUnits.value
    } else {
      width = drawerWidth + 1
    }
  }
  return pp
}
