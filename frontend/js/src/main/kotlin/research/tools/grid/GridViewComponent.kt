package research.tools.grid

import com.ccfraser.muirwik.components.*
import kotlinx.css.*
import kotlinx.css.properties.Timing
import kotlinx.css.properties.ms
import kotlinx.css.properties.transition
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onMouseOutFunction
import kotlinx.html.js.onMouseOverFunction
import model.Grid
import model.GridType
import react.*
import react.dom.div
import research.tools.grid.CutsGridComponent.GridStyles.squareCss
import research.tools.grid.CutsGridComponent.GridStyles.squareHoverCss
import styled.StyleSheet
import styled.StyledElementBuilder
import styled.css
import styled.styledDiv

class CutsGridComponent : RComponent<CutsGridProps, CutsGridState>() {

  override fun RBuilder.render() {

    themeContext.Consumer { theme ->
      mPaper {
        div {
          attrs {
            onMouseOutFunction = {
              setState {
                firstHover = false
                secondHover = false
                thirdHover = false
                fourthHover = false
              }
            }
          }
          mGridContainer {
            css {
              display = Display.flex
              flexDirection = FlexDirection.column
            }

            mGridItem {
              mGridContainer {
                gridItem(
                  predicate = state.firstHover,
                  onClick = { handleClick(GridType.Single) },
                  onMouseOver = { setState { firstHover = true } }
                )
                gridItem(
                  predicate = state.secondHover,
                  onClick = { handleClick(GridType.TwoHorizontal) },
                  onMouseOver = {
                    setState {
                      secondHover = true
                      firstHover = true
                    }
                  })
              }
            }
            mGridItem {
              mGridContainer {
                gridItem(
                  predicate = state.thirdHover,
                  onClick = { handleClick(GridType.TwoVertical) },
                  onMouseOver = {
                    setState {
                      thirdHover = true
                      firstHover = true
                    }
                  })
                gridItem(
                  predicate = state.fourthHover,
                  onClick = { handleClick(GridType.Four) },
                  onMouseOver = {
                    setState {
                      firstHover = true
                      secondHover = true
                      thirdHover = true
                      fourthHover = true
                    }
                  })
              }
            }
          }
        }
      }
    }
  }

  private fun handleClick(grid: GridType) {
    props.onClick(grid)
  }

  private fun StyledElementBuilder<MGridProps>.gridItem(
    predicate: Boolean,
    onClick: () -> Unit,
    onMouseOver: () -> Unit
  ) {
    styledDiv {
      attrs {
        this.onClickFunction = { onClick() }
        this.onMouseOverFunction = { onMouseOver() }
      }
      when {
        predicate -> css(squareHoverCss)
        else -> css(squareCss)
      }
    }
  }

  object GridStyles : StyleSheet("ComponentStyles", isStatic = true) {
    val squareCss by css {
      flex(1.0)
      flexDirection = FlexDirection.column
      width = 25.px
      height = 25.px
      border = "1px solid grey"
      margin(1.px)
      transition("all", 200.ms, Timing.easeOut)
    }

    val squareHoverCss by css {
      flex(1.0)
      flexDirection = FlexDirection.column
      width = 25.px
      height = 25.px
      border = "1px solid grey"
      margin(1.px)
      transition("all", 200.ms, Timing.easeOut)
      backgroundColor = Colors.Pink.shade500
    }
  }

}

interface CutsGridProps : RProps {
  var current: Grid
  var onClick: (GridType) -> Unit
}

class CutsGridState(
  var firstHover: Boolean = false,
  var secondHover: Boolean = false,
  var thirdHover: Boolean = false,
  var fourthHover: Boolean = false
) : RState

fun RBuilder.grid(
  current: Grid,
  onClick: (GridType) -> Unit
) = child(CutsGridComponent::class) {
  attrs.onClick = onClick
  attrs.current = current
}
