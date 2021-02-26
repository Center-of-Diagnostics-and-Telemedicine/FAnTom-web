package research.marks

import com.ccfraser.muirwik.components.table.MTableCellAlign
import com.ccfraser.muirwik.components.table.MTableRowProps
import com.ccfraser.muirwik.components.table.mTableCell
import kotlinx.css.padding
import styled.StyledElementBuilder
import styled.css

fun StyledElementBuilder<MTableRowProps>.tableCell(text: String) {
  mTableCell(align = MTableCellAlign.center) {
    css {
      padding = "8px"
    }
    +text
  }
}