package research

import com.ccfraser.muirwik.components.*
import kotlinext.js.jsObject
import react.RBuilder

fun RBuilder.toolsDrawer(
  open: Boolean,
  drawerWidth: Int,
  onOpen: () -> Unit,
  onClose: () -> Unit,
  block: RBuilder.() -> Unit
) {
  mDrawer(
    open,
    MDrawerAnchor.left,
    MDrawerVariant.permanent,
    paperProps = mPaperProps(open, drawerWidth = drawerWidth)
  ) {
    attrs {
      onMouseEnter = { onOpen() }
      onMouseLeave = { onClose() }
    }

    block()
  }
}

private fun mPaperProps(open: Boolean, drawerWidth: Int): MPaperProps {
  val pp: MPaperProps = jsObject { }
  pp.asDynamic().style = kotlinext.js.js {
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
