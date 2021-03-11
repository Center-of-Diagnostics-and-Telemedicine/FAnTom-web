package research.marks

import com.ccfraser.muirwik.components.table.MTableCellAlign
import com.ccfraser.muirwik.components.table.mTableCell
import kotlinx.css.padding
import react.RBuilder
import styled.css

fun RBuilder.tableCell(text: String) {
  mTableCell(align = MTableCellAlign.center) {
    css {
      padding = "8px"
    }
    +text
  }
}