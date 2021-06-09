package decompose.research.tools

import com.ccfraser.muirwik.components.*
import components.grid.Grid
import decompose.RenderableComponent
import decompose.research.tools.GridUi.GridStyles.squareCss
import decompose.research.tools.GridUi.GridStyles.squareHoverCss
import decompose.research.tools.GridUi.State
import kotlinx.css.*
import kotlinx.css.properties.Timing
import kotlinx.css.properties.ms
import kotlinx.css.properties.transition
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onMouseOutFunction
import kotlinx.html.js.onMouseOverFunction
import model.GridType
import react.RBuilder
import react.RState
import react.dom.div
import react.setState
import root.debugLog
import styled.StyleSheet
import styled.StyledElementBuilder
import styled.css
import styled.styledDiv

class GridUi(props: Props<Grid>) : RenderableComponent<Grid, State>(
  props = props,
  initialState = State(
    props.component.model.value
  )
) {

  init {
    component.model.bindToState { model = it }
  }

  override fun RBuilder.render() {
    debugLog("GridUi render called")
    debugLog(state.model.grid.toString())
    themeContext.Consumer { theme ->
      mPaper {
        div {
          attrs {
            onMouseOutFunction = {
              setState {
                firstHover = false//state.model.grid.types.isNotEmpty()
                secondHover = false//state.model.grid.types.size > 1
                thirdHover = false//state.model.grid.types.size > 2
                fourthHover = false//state.model.grid.types.size > 3
              }
            }
//            onMouseOverFunction = {
//              setState {
//                firstHover = false
//                secondHover = false
//                thirdHover = false
//                fourthHover = false
//              }
//            }
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
                  onClick = { component.changeGrid(GridType.Single) },
                  onMouseOver = { setState { firstHover = true } }
                )
                gridItem(
                  predicate = state.secondHover,
                  onClick = { component.changeGrid(GridType.TwoHorizontal) },
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
                  onClick = { component.changeGrid(GridType.TwoVertical) },
                  onMouseOver = {
                    setState {
                      thirdHover = true
                      firstHover = true
                    }
                  })
                gridItem(
                  predicate = state.fourthHover,
                  onClick = { component.changeGrid(GridType.Four) },
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

  object GridStyles : StyleSheet("GridComponentStyles", isStatic = true) {
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

  class State(
    var model: Grid.Model,
    var firstHover: Boolean = false,
    var secondHover: Boolean = false,
    var thirdHover: Boolean = false,
    var fourthHover: Boolean = false
  ) : RState

}