package research.marks

import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.MIconButtonSize
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.form.MFormControlMargin
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.menu.mMenu
import com.ccfraser.muirwik.components.menu.mMenuItem
import com.ccfraser.muirwik.components.table.MTableCellAlign
import com.ccfraser.muirwik.components.table.MTableCellPadding
import com.ccfraser.muirwik.components.table.mTableCell
import com.ccfraser.muirwik.components.table.mTableRow
import com.ccfraser.muirwik.components.themeContext
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
      val area = props.mark
      mTableRow(
        key = area.id,
        onClick = {
          props.eventOutput(MarksView.Event.SelectItem(area))
        }
      ) {
        if (area.selected) {
          css {
            backgroundColor = Color(theme.palette.primary.main)
          }
        }
        mTableCell(align = MTableCellAlign.center, padding = MTableCellPadding.none) {
          +"${round(area.markData.x)}"
        }
        mTableCell(align = MTableCellAlign.center, padding = MTableCellPadding.none) {
          +"${round(area.markData.y)}"
        }
        if (props.isPlanar) {
          mTableCell(align = MTableCellAlign.center, padding = MTableCellPadding.none) {
            +"${round(area.markData.sizeVertical)}"
          }
          mTableCell(align = MTableCellAlign.center, padding = MTableCellPadding.none) {
            +"${round(area.markData.sizeHorizontal)}"
          }
        } else {
          mTableCell(align = MTableCellAlign.center, padding = MTableCellPadding.none) {
            +"${round(area.markData.z)}"
          }
          mTableCell(align = MTableCellAlign.center, padding = MTableCellPadding.none) {
            +"${round(area.markData.sizeHorizontal)}"
          }
        }

        mTableCell(align = MTableCellAlign.center, padding = MTableCellPadding.none) {
          mButton(
            caption = if (area.type == null) "тип" else area.type!!.ru.take(3),
            onClick = { handleShowMenuClick(it, area.id) },
            size = MButtonSize.small
          )
          mMenu(
            state.selectedMenuIndex == area.id,
            anchorElement = anchorElement,
            onClose = { _, reason -> handleOnClose() }) {

            props.markTypes.forEach { markType ->
              mMenuItem(
                primaryText = markType.ru,
                onClick = {
                  it.stopPropagation()
                  handleSimpleClick(markType)
                })
            }
          }
        }
        mTableCell(align = MTableCellAlign.center, padding = MTableCellPadding.none) {
          mIconButton(
            "close",
            onClick = { props.eventOutput(MarksView.Event.DeleteItem(area)) },
            size = MIconButtonSize.small
          )
        }
      }
      if (area.selected) {
        mTableRow {
          if (state.writing) {
            mTableCell(colSpan = 5) {
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
          } else {
            mTableCell(colSpan = 6) {
              commentText()
            }
          }

        }
      }
    }
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
      anchorElement = currentTarget.asDynamic()
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
