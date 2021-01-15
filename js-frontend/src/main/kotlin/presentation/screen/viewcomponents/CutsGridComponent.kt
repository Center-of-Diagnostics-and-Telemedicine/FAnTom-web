package presentation.screen.viewcomponents

import model.CutsGridType
import com.ccfraser.muirwik.components.*
import kotlinx.css.*
import kotlinx.css.properties.Timing
import kotlinx.css.properties.ms
import kotlinx.css.properties.transition
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onMouseOutFunction
import kotlinx.html.js.onMouseOverFunction
import react.*
import react.dom.div
import styled.StyleSheet
import styled.StyledElementBuilder
import styled.css
import styled.styledDiv

class CutsGridComponent : RComponent<CutsGridProps, CutsGridState>() {

  override fun RBuilder.render() {

    themeContext.Consumer { theme ->
      val themeStyles = object : StyleSheet("ComponentStyles", isStatic = true) {
        val square by css {
          flex(1.0)
          flexDirection = FlexDirection.column
          width = 20.px
          height = 20.px
          border = "1px solid grey"
          margin(1.px)
          transition("all", 200.ms, Timing.easeOut)
        }

        val squareHover by css {
          flex(1.0)
          flexDirection = FlexDirection.column
          width = 20.px
          height = 20.px
          border = "1px solid grey"
          margin(1.px)
          transition("all", 200.ms, Timing.easeOut)
          backgroundColor = Colors.Pink.shade500
        }
      }



      mPaper {
        div {
          attrs {
            this.onMouseOutFunction = {
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
                  squareHover = themeStyles.squareHover,
                  themeStyle = themeStyles.square,
                  predicate = state.firstHover,
                  onClick = { props.cutsGridClickListener(CutsGridType.SINGLE) },
                  onMouseOver = { setState { firstHover = true } }
                )
                gridItem(
                  squareHover = themeStyles.squareHover,
                  themeStyle = themeStyles.square,
                  predicate = state.secondHover,
                  onClick = { props.cutsGridClickListener(CutsGridType.TWO_HORIZONTAL) },
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
                  squareHover = themeStyles.squareHover,
                  themeStyle = themeStyles.square,
                  predicate = state.thirdHover,
                  onClick = { props.cutsGridClickListener(CutsGridType.TWO_VERTICAL) },
                  onMouseOver = {
                    setState {
                      thirdHover = true
                      firstHover = true
                    }
                  })
                gridItem(
                  squareHover = themeStyles.squareHover,
                  themeStyle = themeStyles.square,
                  predicate = state.fourthHover,
                  onClick = { props.cutsGridClickListener(CutsGridType.THREE) },
                  onMouseOver = {
                    setState {
                      fourthHover = true
                      thirdHover = true
                      firstHover = true
                    }
                  })
              }
            }
          }
        }
      }
    }
  }

  private fun StyledElementBuilder<MGridProps>.gridItem(
    squareHover: RuleSet,
    themeStyle: RuleSet,
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
        predicate -> css(squareHover)
        else -> css(themeStyle)
      }
    }
  }

}

interface CutsGridProps : RProps {
  var cutsGridClickListener: (CutsGridType) -> Unit
}

class CutsGridState(
  var firstHover: Boolean = false,
  var secondHover: Boolean = false,
  var thirdHover: Boolean = false,
  var fourthHover: Boolean = false
) : RState

fun RBuilder.cutsGrid(
  cutsGridClickListener: (CutsGridType) -> Unit
) = child(CutsGridComponent::class) {
  attrs.cutsGridClickListener = cutsGridClickListener
}