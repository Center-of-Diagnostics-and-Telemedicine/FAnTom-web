package presentation.screen.viewcomponents

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItemWithIcon
import model.SelectedArea
import kotlinx.css.*
import kotlinx.html.Tag
import kotlinx.html.classes
import kotlinx.html.js.onClickFunction
import kotlinx.html.role
import react.RBuilder
import react.RProps
import react.ReactElement
import react.dom.*
import styled.css
import styled.styledDiv
import kotlin.math.round

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


fun RBuilder.loading(isLoading: Boolean) = if (isLoading) {
  mLinearProgress {
    css {
      position = Position.absolute
      top = 0.px
      left = 0.px
      width = 100.pct
      zIndex = 100
    }
  }
} else {
  EmptyElement
}

object EmptyElement : ReactElement {
  override val props = object : RProps {}
}

fun RDOMBuilder<Tag>.selectedArea(
  deleteListener: (area: SelectedArea) -> Unit,
  data: SelectedArea
) {
  tr {
    td { +round(data.x).toString() }
    td { +round(data.y).toString() }
    td { +round(data.z).toString() }
    td { +round(data.size).toString() }
    td { options() }
    td {
      button {
        +"X"
        attrs.onClickFunction = { deleteListener(data) }
      }
    }
  }
}

fun RDOMBuilder<Tag>.options() {
  select {
    attrs.size = "1"
    option {
      attrs.selected = true
      +"тип"
    }
    option { +"С" }
    option { +"П" }
    option { +"М" }
    option { +"Н.О." }
  }
}

fun RDOMBuilder<Tag>.mipMethodBtn(
  name: String,
  mipMethodOfState: Int,
  mipMethod: Int,
  changeListener: (Int) -> Unit
) {
  button(classes = "btn") {
    +name

    attrs.onClickFunction = { changeListener(mipMethod) }
    if (mipMethodOfState == mipMethod) {
      attrs.classes = setOf("btn btn-primary menu-button ")
    } else {
      attrs.classes = setOf("btn btn-outline-primary menu-button ")
    }
  }
}

fun RBuilder.spacer() {
  themeContext.Consumer { theme ->
    // This puts in a spacer to get below the AppBar.
    styledDiv {
      css {
        toolbarJsCssToPartialCss(theme.mixins.toolbar)
      }
    }
    mDivider { }
  }
}

fun RBuilder.listMenu(fullWidth: Boolean, drawerWidth: Int): ReactElement {
  return themeContext.Consumer { theme ->
    div {
      attrs {
        role = "button"
      }
    }
    mList {
      css {
        backgroundColor = Color(theme.palette.background.paper)
        width = if (fullWidth) LinearDimension.auto else drawerWidth.px
      }
      mListItemWithIcon("search", "Просмотренные", divider = false)
      mListItemWithIcon("done", "Оконченные", divider = false)
    }
  }
}
