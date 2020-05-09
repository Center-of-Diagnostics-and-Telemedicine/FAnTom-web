package presentation.screen.viewcomponents

import com.ccfraser.muirwik.components.*
import presentation.screen.ComponentStyles
import presentation.screen.research.cut.cutContainer
import model.CellModel
import kotlinx.css.*
import react.*
import styled.css
import styled.styledDiv

class ThreeCutsContainerComponent :
  RComponent<ThreeCutsContainerProps, ThreeCutsContainerState>() {

  override fun RBuilder.render() {
    if (props.cells.isNotEmpty()) {
      themeContext.Consumer { theme ->
        val topLeftCellModel = props.cells[0]
        val bottomLeftCellModel = props.cells[1]
        val bottomRightCellModel = props.cells[2]
        //это колонка строк срезов(верх низ)
        styledDiv {
          css(ComponentStyles.columnOfRowsStyle)


          //одна из строк (лево/право)
          styledDiv {
            css(ComponentStyles.rowOfColumnsStyle)

            //контейнер для среза
            cutContainer(topLeftCellModel, props.doubleClickListener)
            styledDiv {
              css {
                display = Display.flex
                flexDirection = FlexDirection.row
                position = Position.relative
                width = 50.pct
              }
//              ctTypeButtons(topLeftCellModel)
            }

          }

          styledDiv {
            css(ComponentStyles.rowOfColumnsStyle)

            cutContainer(bottomLeftCellModel, props.doubleClickListener)
            cutContainer(bottomRightCellModel, props.doubleClickListener)
          }
        }
      }
    }
  }
}

interface ThreeCutsContainerProps : RProps {
  var cells: List<CellModel>
  var doubleClickListener: (CellModel) -> Unit
}

class ThreeCutsContainerState(
) : RState

fun RBuilder.threeCutsContainer(
  cells: List<CellModel>,
  doubleClickListener: (CellModel) -> Unit
) = child(ThreeCutsContainerComponent::class) {
  attrs.cells = cells
  attrs.doubleClickListener = doubleClickListener
}