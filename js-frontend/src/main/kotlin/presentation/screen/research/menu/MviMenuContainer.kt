package presentation.screen.research.menu

import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemWithIcon
import com.ccfraser.muirwik.components.spacingUnits
import com.ccfraser.muirwik.components.themeContext
import com.ccfraser.muirwik.components.transitions.mCollapse
import kotlinx.css.paddingLeft
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.StyleSheet
import styled.css

class MviMenuContainer : RComponent<MenuProps, MenuState>() {

  init {
    state = MenuState()
  }

  override fun RBuilder.render() {
    themeContext.Consumer { theme ->
      val themeStyles = object : StyleSheet("ComponentStyles", isStatic = true) {

        val nested by css {
          paddingLeft = 4.spacingUnits

        }
      }
      mList {
        mListItemWithIcon(primaryText = "MIP", iconName = "line_weight")
        mCollapse(show = props.isOpen) {
          css(themeStyles.nested)
          mList {
            mListItem {
              mviMipMethod()
            }
            mListItem {
              mviMipValue()
            }
          }
        }

        mListItemWithIcon(primaryText = "Яркость и контраст", iconName = "tonality")
        mCollapse(show = props.isOpen) {
          css(themeStyles.nested)
          mListItem {
            mviWhite()
          }
          mListItem {
            mviBlack()
          }
          mListItem {
            mviGamma()
          }
        }

        mListItemWithIcon(primaryText = "Шаблоны яркости и контраста", iconName = "palette")
        mCollapse(show = props.isOpen) {
          css(themeStyles.nested)
          mListItem {
            mviPreset()
          }
        }
      }
    }
  }
}

interface MenuProps : RProps {
  var isOpen: Boolean
}

class MenuState : RState

fun RBuilder.leftMenu(isOpen: Boolean) = child(MviMenuContainer::class) {
  attrs.isOpen = isOpen
}