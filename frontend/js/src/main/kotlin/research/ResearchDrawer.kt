package research

import com.ccfraser.muirwik.components.*
import kotlinext.js.jsObject
import kotlinx.css.pct
import kotlinx.css.vh
import react.RBuilder

fun RBuilder.leftDrawer(
  open: Boolean,
  drawerWidth: Int,
  onOpen: () -> Unit,
  onClose: () -> Unit,
  block: RBuilder.() -> Unit
) {
  drawer(open, drawerWidth, onOpen, onClose, MDrawerAnchor.left, block)
}

fun RBuilder.rightDrawer(
  open: Boolean,
  drawerWidth: Int,
  onOpen: () -> Unit,
  onClose: () -> Unit,
  block: RBuilder.() -> Unit
) {
  drawer(open, drawerWidth, onOpen, onClose, MDrawerAnchor.right, block)
}

private fun RBuilder.drawer(
  open: Boolean,
  drawerWidth: Int,
  onOpen: () -> Unit,
  onClose: () -> Unit,
  anchor: MDrawerAnchor,
  block: RBuilder.() -> Unit
) {
  mDrawer(
    open,
    anchor,
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
    height = 100.pct
    minHeight = 100.vh
    if (open.not()) {
      overflowX = "hidden"
      width = 7.spacingUnits.value
    } else {
      width = drawerWidth + 1
    }
  }
  return pp
}
