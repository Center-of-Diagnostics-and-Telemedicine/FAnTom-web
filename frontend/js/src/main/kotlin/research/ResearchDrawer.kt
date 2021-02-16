package research

import com.ccfraser.muirwik.components.MDrawerAnchor
import com.ccfraser.muirwik.components.MDrawerVariant
import com.ccfraser.muirwik.components.MPaperProps
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.mDrawer
import kotlinext.js.jsObject
import kotlinx.css.*
import react.RBuilder
import styled.css
import styled.styledDiv

fun RBuilder.leftDrawer(
  open: Boolean,
  drawerWidth: LinearDimension,
  onOpen: () -> Unit?,
  onClose: () -> Unit?,
  block: RBuilder.() -> Unit
) {
  drawer(open, drawerWidth, onOpen, onClose, MDrawerAnchor.left, block)
}

fun RBuilder.rightDrawer(
  open: Boolean,
  drawerWidth: LinearDimension,
  onOpen: () -> Unit?,
  onClose: () -> Unit?,
  block: RBuilder.() -> Unit
) {
  drawer(open, drawerWidth, onOpen, onClose, MDrawerAnchor.right, block)
}

fun RBuilder.leftDrawerHeaderButton(open: Boolean, onClick: () -> Unit) {
  styledDiv {
    css {
      display = Display.flex
      alignItems = Align.center
      justifyContent = JustifyContent.flexEnd
    }
    mIconButton(
      if (open) "chevron_left" else "chevron_right",
      onClick = { onClick() }
    )
  }
}

fun RBuilder.rightDrawerHeaderButton(open: Boolean, onClick: () -> Unit) {
  styledDiv {
    css {
      display = Display.flex
      alignItems = Align.center
      justifyContent = JustifyContent.flexStart
    }
    mIconButton(
      if (open) "chevron_right" else "chevron_left",
      onClick = { onClick() }
    )
  }
}

private fun RBuilder.drawer(
  open: Boolean,
  drawerWidth: LinearDimension,
  onOpen: () -> Unit?,
  onClose: () -> Unit?,
  anchor: MDrawerAnchor,
  block: RBuilder.() -> Unit
) {
  mDrawer(
    open,
    anchor,
    MDrawerVariant.permanent,
    paperProps = mPaperProps(open, drawerWidth = drawerWidth)
  ) {
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
