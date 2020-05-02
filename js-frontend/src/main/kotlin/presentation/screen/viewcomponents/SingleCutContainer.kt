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


class SingleCutContainerComponent : RComponent<SingleCutContainerProps, SingleCutContainerState>() {

  override fun RBuilder.render() {
    val cellModel = props.cellModel
    themeContext.Consumer { theme ->
      styledDiv {
        css(ComponentStyles.rowOfColumnsStyle)
        cutContainer(cellModel, props.doubleClickListener)
      }
    }
  }

}

interface SingleCutContainerProps : RProps {
  var cellModel: CellModel
  var doubleClickListener: (CellModel) -> Unit
}

class SingleCutContainerState(
) : RState

fun RBuilder.singleCutContainer(
  cellModel: CellModel,
  doubleClickListener: (CellModel) -> Unit
) = child(SingleCutContainerComponent::class) {
  attrs.cellModel = cellModel
  attrs.doubleClickListener = doubleClickListener

}