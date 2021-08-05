package decompose.research.marks

import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.MIconButtonSize
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.mDivider
import com.ccfraser.muirwik.components.mIcon
import com.ccfraser.muirwik.components.mPaper
import com.ccfraser.muirwik.components.table.*
import com.ccfraser.muirwik.components.themeContext
import components.researchmarks.ResearchMarks
import components.researchmarks.ResearchMarks.Model
import decompose.Props
import decompose.RenderableComponent
import decompose.research.marks.MarksUi.State
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import kotlinx.css.padding
import model.MarkModel
import react.RBuilder
import react.RState
import research.marks.tableCell
import styled.css
import kotlin.math.round

class MarksUi(props: Props<ResearchMarks>) : RenderableComponent<ResearchMarks, State>(
  props = props,
  initialState = State(model = props.component.model.value)
) {

  init {
    component.model.bindToState { model = it }
  }

  override fun RBuilder.render() {
    mDivider()

    mPaper {
      mTable {
        mTableHead {
          mTableRow {
            mTableCell(align = MTableCellAlign.center) {
              css { padding = "8px" }
              mIcon("visibility")
            }
            tableCell("X")
            tableCell("Y")

            tableCell("mm_v")
            tableCell("mm_h")
            tableCell("Z")
            tableCell("mm")
            tableCell("Тип")
            tableCell("")
          }
        }
        mTableBody {
          state.model.marks.forEach { area ->
            markDataRow(area)
          }
        }
      }
    }
  }

  fun RBuilder.markDataRow(area: MarkModel) {
    themeContext.Consumer { theme ->
      mTableRow(
        key = area.id,
        onClick = { component.onSelectItem(area.id) }
      ) {
        if (area.selected) {
          css {
            backgroundColor = Color(theme.palette.primary.main)
          }
        }
//      mTableCell(align = MTableCellAlign.center, padding = MTableCellPadding.checkbox) {
//        visibilityButton(area)
//      }
        tableCell("${round(area.markData.x)}")
        tableCell("${round(area.markData.y)}")
        tableCell("${round(area.markData.sizeVertical)}")
        tableCell("${round(area.markData.sizeHorizontal)}")

        mTableCell(align = MTableCellAlign.center, padding = MTableCellPadding.none) {
          menuButton(area)
//          menu(area)
        }
        mTableCell(align = MTableCellAlign.center, padding = MTableCellPadding.checkbox) {
          closeButton(area)
        }
      }
    }
  }

//  private fun RBuilder.comment(area: MarkModel) {
//    styledDiv {
//      css {
//        display = Display.flex
//        flexDirection = FlexDirection.row
//        alignItems = Align.center
//        justifyContent = JustifyContent.spaceBetween
//      }
//      commentInput()
//      mIconButton("done", onClick = {
//        debugLog(state.comment)
//        props.eventOutput(MarksView.Event.ItemCommentChanged(area, state.comment))
//        setState { writing = false }
//      })
//    }
//  }

  private fun RBuilder.closeButton(area: MarkModel) {
    mIconButton(
      "close",
      onClick = {
        it.stopPropagation()
        component.onDeleteItem(area.id)
      },
      size = MIconButtonSize.small
    )
  }

//  private fun RBuilder.menu(area: MarkModel) {
//    mMenu(
//      state.selectedMenuIndex == area.id,
//      anchorElement = anchorElement,
//      onClose = { _, _ -> handleOnClose() }
//    ) {
//
//      props.markTypes.forEach { markType ->
//        mMenuItem(
//          primaryText = markType.en,
//          onClick = {
//            it.stopPropagation()
//            handleSimpleClick(markType)
//          })
//      }
//    }
//  }

  private fun RBuilder.menuButton(area: MarkModel) {
    mButton(
      caption = if (area.type == null) "тип" else area.type!!.en.take(3),
      onClick = {
        it.stopPropagation()
//        handleShowMenuClick(it, area.id)
      },
      size = MButtonSize.small
    )
  }

//  private fun RBuilder.visibilityButton(area: MarkModel) {
//    mIconButton(
//      if (area.visible) "visibility" else "visibility_off",
//      onClick = {
//        it.stopPropagation()
//        props.eventOutput(MarksView.Event.ChangeVisibility(area))
//      },
//      size = MIconButtonSize.small
//    )
//  }
//
//  private fun RBuilder.commentInput() {
//    mTextField(
//      value = state.comment,
//      variant = MFormControlVariant.outlined,
//      fullWidth = true,
//      label = "Комментарий",
//      onChange = ::onCommentChanged,
//      margin = MFormControlMargin.none
//    )
//  }
//
//  private fun RBuilder.commentText() {
//    mTypography {
//      +state.comment
//      attrs.onClick = { setState { writing = true } }
//    }
//  }

  class State(var model: Model) : RState
}