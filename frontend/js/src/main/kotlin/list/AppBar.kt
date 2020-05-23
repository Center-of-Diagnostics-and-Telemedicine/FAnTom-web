package list

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mIconButton
import kotlinx.css.zIndex
import react.RBuilder
import styled.css

fun RBuilder.appBar(onClick: () -> Unit) {
  themeContext.Consumer { theme ->
    mAppBar(position = MAppBarPosition.absolute) {
      css { zIndex = theme.zIndex.drawer + 1 }
      mToolbar {
        mIconButton("menu", color = MColor.inherit, onClick = { onClick() })
        mToolbarTitle("Fantom")
      }
    }
  }
}
