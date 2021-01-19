package research

import com.ccfraser.muirwik.components.*
import kotlinext.js.jsObject
import kotlinx.css.LinearDimension
import kotlinx.css.pct
import kotlinx.css.px
import kotlinx.css.vh
import react.RBuilder

fun RBuilder.leftDrawer(
  open: Boolean,
  drawerWidth: LinearDimension,
  onOpen: () -> Unit,
  onClose: () -> Unit,
  block: RBuilder.() -> Unit
) {
  drawer(open, drawerWidth, onOpen, onClose, MDrawerAnchor.left, block)
}

fun RBuilder.rightDrawer(
  open: Boolean,
  drawerWidth: LinearDimension,
  onOpen: () -> Unit,
  onClose: () -> Unit,
  block: RBuilder.() -> Unit
) {
  drawer(open, drawerWidth, onOpen, onClose, MDrawerAnchor.right, block)
}

private fun RBuilder.drawer(
  open: Boolean,
  drawerWidth: LinearDimension,
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

private fun mPaperProps(open: Boolean, drawerWidth: LinearDimension): MPaperProps {
  val pp: MPaperProps = jsObject { }
  pp.asDynamic().style = kotlinext.js.js {
    height = 100.pct
    minHeight = 100.vh
    overflowX = "hidden"
    width = drawerWidth + 1.px
  }
  return pp
}
