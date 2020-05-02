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
//
//  private fun RBuilder.ctTypeButtons(topLeftCellModel: CellModel) {
//    mGridContainer {
//      mGridItem(xs = MGridSize.cells12) {
//        buttonWithPopover(
//          text = "КТ-0\nНулевая",
//          color = Color("#78b980"),
//          popoverText = "Норма и отсутствие КТ-признаков вирусной пневмонии на фоне типичной клинической картины и релевантного эпидемиологического анамнеза"
//        )
//      }
//      mGridItem(xs = MGridSize.cells12) {
//        buttonWithPopover(
//          text = "КТ-1\nЛегкая",
//          color = Color("#b7d389"),
//          popoverText = "Зоны уплотнения по типу \"матового стекла\" \nВовлечение паренхимы легкого =< 25% \nЛибо отсутствие КТ-признаков на фоне типичной клинической картины и релевантного эпидемиологического анамнеза"
//        )
//      }
//      mGridItem(xs = MGridSize.cells12) {
//        buttonWithPopover(
//          text = "КТ-2\nСредне-тяжелая",
//          color = Color("#fae991"),
//          popoverText = "Зоны уплотнения по типу \"матового стекла\" \nВовлечение паренхимы легкого 25-50%"
//        )
//      }
//      mGridItem(xs = MGridSize.cells12) {
//        buttonWithPopover(
//          text = "КТ-3\nТяжелая",
//          color = Color("#eeac7d"),
//          popoverText = "Зоны уплотнения по типу \"матового стекла\" \nЗоны консолидации \nВовлечение паренхимы легкого 50-75% \nУвеличение объема поражения 50% за 24-48 часов на фоне дыхательных нарушений, если исследования выполняются в динамике"
//        )
//      }
//      mGridItem(xs = MGridSize.cells12) {
//        buttonWithPopover(
//          text = "КТ-4\nКритическая",
//          color = Color("#e4706d"),
//          popoverText = "Диффузное уплотнение легочной ткани по типу \"матового стекла\" и консолидации в сочетании с ретикулярными изменениями \nГидроторакс (двусторонний, преобладает слева) \nВовлечение паренхимы легкого >= 75%"
//        )
//      }
//    }
//  }
//
//  private fun RBuilder.buttonWithPopover(
//    text: String,
//    color: Color,
//    popoverText: String
//  ) {
//    styledDiv {
//      css {
//        padding(1.spacingUnits)
//        height = 100.pct
//      }
//      mTooltip(title = popoverText, placement = TooltipPlacement.left) {
//        mButton(text, color = MColor.default) {
//          css {
//            height = 100.pct
//            width = 100.pct
//            backgroundColor = color
//          }
//        }
//      }
//    }
//  }

}

interface ThreeCutsContainerProps : RProps {
  var cells: List<CellModel>
  var doubleClickListener: (CellModel) -> Unit
//  var onCTTypeCLick: (CTType) -> Unit
}

class ThreeCutsContainerState(
) : RState

fun RBuilder.threeCutsContainer(
  cells: List<CellModel>,
  doubleClickListener: (CellModel) -> Unit
//  onCTTypeCLick: (CTType) -> Unit
) = child(ThreeCutsContainerComponent::class) {
  attrs.cells = cells
  attrs.doubleClickListener = doubleClickListener
//  attrs.onCTTypeCLick = onCTTypeCLick
}