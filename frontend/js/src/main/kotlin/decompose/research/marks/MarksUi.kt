package decompose.research.marks

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.table.mTable
import com.ccfraser.muirwik.components.table.mTableBody
import com.ccfraser.muirwik.components.table.mTableHead
import com.ccfraser.muirwik.components.table.mTableRow
import components.researchmarks.ResearchMarks
import decompose.RenderableComponent
import react.RBuilder
import react.RState
import research.marks.tableCell
import decompose.research.marks.MarksUi.State

class MarksUi(props: Props<ResearchMarks>) : RenderableComponent<ResearchMarks, State>(
  props = props,
  initialState = State(
    model = props.component.models.value,
    open = false
  )
) {

  init {
    component.models.bindToState { model = it }
  }

  override fun RBuilder.render() {
    mDivider()

    mPaper {
      mTable {
        mTableHead {
          mTableRow {
//              mTableCell(align = MTableCellAlign.center) {
//                css { padding = "8px" }
//                mIcon("visibility")
//              }
            tableCell("X")
            tableCell("Y")

//            if (props.dependencies.isPlanar) {
//              tableCell("mm_v")
//              tableCell("mm_h")
//            } else {
              tableCell("Z")
              tableCell("mm")
//            }
            tableCell("Тип")
            tableCell("")
          }
        }
        mTableBody {
//          state.items.forEach { area ->
//            markView(
//              mark = area,
//              eventOutput = { marksViewDelegate.dispatch(it) },
//              markTypes = state.model.markTypes,
//              isPlanar = props.dependencies.isPlanar
//            )
//          }
        }
      }
    }
  }

  class State(
    var model: ResearchMarks.Model,
    var open: Boolean
  ) : RState
}