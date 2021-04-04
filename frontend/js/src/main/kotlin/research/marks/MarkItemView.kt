package research.marks

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.*
import com.ccfraser.muirwik.components.form.MFormControlMargin
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.ccfraser.muirwik.components.menu.mMenu
import com.ccfraser.muirwik.components.menu.mMenuItem
import com.ccfraser.muirwik.components.styles.Theme
import com.ccfraser.muirwik.components.table.MTableCellAlign
import com.ccfraser.muirwik.components.table.MTableCellPadding
import com.ccfraser.muirwik.components.table.mTableCell
import com.ccfraser.muirwik.components.table.mTableRow
import kotlinx.css.*
import model.MarkModel
import model.MarkTypeModel
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.Node
import org.w3c.dom.events.Event
import react.*
import root.debugLog
import styled.css
import styled.styledDiv
import view.MarksView
import kotlin.math.round

class MarkItemView(prps: MarkItemProps) : RComponent<MarkItemProps, MarkItemState>(prps) {

  private var anchorElement: Node? = null

  init {
    val comment = props.mark.comment
    state = MarkItemState(comment = comment, writing = comment.isEmpty())
  }

  override fun RBuilder.render() {
    themeContext.Consumer { theme ->
      markDataRow(theme)
      markAcceptRow(selected = props.mark.selected)
//      markCommentRow(selected = props.mark.selected)
    }
  }

  private fun RBuilder.markAcceptRow(selected: Boolean) {
    if (selected) {
      mTableRow {
        mTableCell(colSpan = 6) {
          mButton(
            caption = "Сохранить",
            variant = MButtonVariant.contained,
            size = MButtonSize.large,
            onClick = { props.eventOutput(MarksView.Event.OnAcceptClick(props.mark)) }
          ) {
            attrs.startIcon = mIcon("done", fontSize = MIconFontSize.small, addAsChild = false)
            attrs.fullWidth = true
          }
        }
      }
    }
  }

  private fun RBuilder.markCommentRow(selected: Boolean) {
    if (selected) {
      mTableRow {
        if (state.writing) {
          mTableCell(colSpan = 7) {
            comment(props.mark)
          }
        } else {
          mTableCell(colSpan = 6) {
            commentText()
          }
        }
      }
    }
  }

  private fun RBuilder.markDataRow(theme: Theme) {
    val area = props.mark
    mTableRow(
      key = area.id,
      onClick = { props.eventOutput(MarksView.Event.SelectItem(area)) }
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
      if (props.isPlanar) {
        tableCell("${round(area.markData.sizeVertical)}")
        tableCell("${round(area.markData.sizeHorizontal)}")
      } else {
        tableCell("${round(area.markData.z)}")
        tableCell("${round(area.markData.sizeHorizontal)}")
      }

      mTableCell(align = MTableCellAlign.center, padding = MTableCellPadding.none) {
        menuButton(area)
        menu(area)
      }
      mTableCell(align = MTableCellAlign.center, padding = MTableCellPadding.checkbox) {
        closeButton(area)
      }
    }
  }

  private fun RBuilder.comment(area: MarkModel) {
    styledDiv {
      css {
        display = Display.flex
        flexDirection = FlexDirection.row
        alignItems = Align.center
        justifyContent = JustifyContent.spaceBetween
      }
      commentInput()
      mIconButton("done", onClick = {
        debugLog(state.comment)
        props.eventOutput(MarksView.Event.ItemCommentChanged(area, state.comment))
        setState { writing = false }
      })
    }
  }

  private fun RBuilder.closeButton(area: MarkModel) {
    mIconButton(
      "close",
      onClick = {
        it.stopPropagation()
        props.eventOutput(MarksView.Event.DeleteItem(area))
      },
      size = MIconButtonSize.small
    )
  }

  private fun RBuilder.menu(area: MarkModel) {
    mMenu(
      state.selectedMenuIndex == area.id,
      anchorElement = anchorElement,
      onClose = { _, _ -> handleOnClose() }
    ) {

      props.markTypes.forEach { markType ->
        mMenuItem(
          primaryText = markType.en,
          onClick = {
            it.stopPropagation()
            handleSimpleClick(markType)
          })
      }
    }
  }

  private fun RBuilder.menuButton(area: MarkModel) {
    mButton(
      caption = if (area.type == null) "тип" else area.type!!.en.take(3),
      onClick = {
        it.stopPropagation()
        handleShowMenuClick(it, area.id)
      },
      size = MButtonSize.small
    )
  }

  private fun RBuilder.visibilityButton(area: MarkModel) {
    mIconButton(
      if (area.visible) "visibility" else "visibility_off",
      onClick = {
        it.stopPropagation()
        props.eventOutput(MarksView.Event.ChangeVisibility(area))
      },
      size = MIconButtonSize.small
    )
  }

  private fun RBuilder.commentInput() {
    mTextField(
      value = state.comment,
      variant = MFormControlVariant.outlined,
      fullWidth = true,
      label = "Комментарий",
      onChange = ::onCommentChanged,
      margin = MFormControlMargin.none
    )
  }

  private fun RBuilder.commentText() {
    mTypography {
      +state.comment
      attrs.onClick = { setState { writing = true } }
    }
  }

  private fun handleShowMenuClick(event: Event, menuIndex: Int) {
    event.stopPropagation()
    val currentTarget = event.currentTarget
    setState {
      anchorElement = currentTarget.asDynamic() as? Node
      selectedMenuIndex = menuIndex
    }
  }

  private fun handleSimpleClick(type: MarkTypeModel) {
    props.eventOutput(MarksView.Event.ChangeMarkType(type, props.mark.id))
    setState {
      selectedMenuIndex = -1
      anchorElement = null
    }
  }

  private fun handleOnClose() {
    setState { anchorElement = null; selectedMenuIndex = -1 }
  }

  private fun onCommentChanged(event: Event) {
    val target = event.target as HTMLInputElement
    val searchTerm = target.value
    setState {
      comment = searchTerm
    }
  }
}

class MarkItemState(
  var selectedMenuIndex: Int = -1,
  var comment: String = "",
  var writing: Boolean
) : RState

interface MarkItemProps : RProps {
  var mark: MarkModel
  var eventOutput: (MarksView.Event) -> Unit
  var markTypes: List<MarkTypeModel>
  var isPlanar: Boolean
}

fun RBuilder.markView(
  mark: MarkModel,
  eventOutput: (MarksView.Event) -> Unit,
  markTypes: List<MarkTypeModel>,
  isPlanar: Boolean
) = child(MarkItemView::class) {
  attrs.mark = mark
  attrs.eventOutput = eventOutput
  attrs.markTypes = markTypes
  attrs.isPlanar = isPlanar
}
