package components

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mIconButton
import react.RBuilder
import react.RProps
import react.ReactElement
import react.dom.div

val altBuilder = RBuilder()

fun RBuilder.alert(message: String = "", open: Boolean = false, handleClose: () -> Unit) =
  if (message.isNotEmpty()) {
    mSnackbar(message = message, open = open,
      onClose = { _, _: MSnackbarOnCloseReason -> handleClose() }) {
      attrs.anchorOriginHorizontal = MSnackbarHorizAnchor.left
      attrs.anchorOriginVertical = MSnackbarVertAnchor.bottom
      attrs.autoHideDuration = 4000
      attrs.action = altBuilder.div {
        mIconButton("close", onClick = { handleClose() }, color = MColor.inherit)
      }
    }
  } else {
    EmptyElement
  }

object EmptyElement : ReactElement {
  override val props = object : RProps {}
}