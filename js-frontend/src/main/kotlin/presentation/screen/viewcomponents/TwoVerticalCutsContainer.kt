package presentation.screen.viewcomponents

import com.ccfraser.muirwik.components.themeContext
import model.CellModel
import presentation.screen.ComponentStyles
import presentation.screen.research.cut.cutContainer
import react.*
import styled.css
import styled.styledDiv

class TwoVerticalCutsContainerComponent :
  RComponent<TwoVerticalCutsContainerProps, TwoVerticalCutsContainerState>() {

  override fun RBuilder.render() {
    if (props.cells.isNotEmpty()) {
      themeContext.Consumer { theme ->
        val topCellModel = props.cells[0]
        styledDiv {
          css(ComponentStyles.rowOfColumnsStyle)
          cutContainer(topCellModel, props.doubleClickListener)
        }

        val bottomCellModel = props.cells[1]
        styledDiv {
          css(ComponentStyles.rowOfColumnsStyle)
          cutContainer(bottomCellModel, props.doubleClickListener)
        }

      }
    }
  }

}

interface TwoVerticalCutsContainerProps : RProps {
  var cells: List<CellModel>
  var doubleClickListener: (CellModel) -> Unit
}

class TwoVerticalCutsContainerState(
) : RState

fun RBuilder.twoVerticalCutsContainer(
  cells: List<CellModel>,
  doubleClickListener: (CellModel) -> Unit
) = child(TwoVerticalCutsContainerComponent::class) {
  attrs.cells = cells
  attrs.doubleClickListener = doubleClickListener
}