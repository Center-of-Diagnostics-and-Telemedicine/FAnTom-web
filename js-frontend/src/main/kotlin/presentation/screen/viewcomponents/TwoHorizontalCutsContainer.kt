package presentation.screen.viewcomponents

import com.ccfraser.muirwik.components.themeContext
import presentation.screen.ComponentStyles
import presentation.screen.research.cut.cutContainer
import model.CellModel
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css
import styled.styledDiv

class TwoHorizontalCutsContainerComponent :
  RComponent<TwoHorizontalCutsContainerProps, TwoHorizontalCutsContainerState>() {

  override fun RBuilder.render() {
    if (props.cells.isNotEmpty()) {
      themeContext.Consumer { theme ->
        val leftCellModel = props.cells[0]
        val rightCellModel = props.cells[1]
        styledDiv {
          css(ComponentStyles.rowOfColumnsStyle)
          cutContainer(leftCellModel, props.doubleClickListener)
          cutContainer(rightCellModel, props.doubleClickListener)
        }
      }
    }
  }

}

interface TwoHorizontalCutsContainerProps : RProps {
  var cells: List<CellModel>
  var doubleClickListener: (CellModel) -> Unit
}

class TwoHorizontalCutsContainerState(
) : RState

fun RBuilder.twoHorizontalCutsContainer(
  cells: List<CellModel>,
  doubleClickListener: (CellModel) -> Unit
) = child(TwoHorizontalCutsContainerComponent::class) {
  attrs.cells = cells
  attrs.doubleClickListener = doubleClickListener
}