package presentation.screen.research.menu

import model.AreaType
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.form.MFormControlMargin
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.menu.mMenu
import com.ccfraser.muirwik.components.menu.mMenuItem
import com.ccfraser.muirwik.components.table.*
import com.ccfraser.muirwik.components.themeContext
import model.SelectedArea
import debugLog
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.Node
import org.w3c.dom.events.Event
import react.*
import styled.StyledElementBuilder
import styled.css
import kotlin.math.round

class ResearchTableRow(props: ResearchTableRowProps) :
  RComponent<ResearchTableRowProps, ResearchTableRowState>(props) {

  private var anchorElement: Node? = null

  init {
    val comment = props.area.comment
    state = ResearchTableRowState(comment = comment, writing = comment.isEmpty())
  }

  override fun RBuilder.render() {
    themeContext.Consumer { theme ->
      val area = props.area
      mTableRow(
        key = props.area.id,
        onClick = {
          props.select()
        }
      ) {
        if (props.selected) {
          css {
            backgroundColor = Color(theme.palette.primary.main)
          }
        }
        mTableCell(align = MTableCellAlign.center) { +"${round(area.x)}" }
        mTableCell(align = MTableCellAlign.center) { +"${round(area.y)}" }
        mTableCell(align = MTableCellAlign.center) { +"${round(area.z)}" }
        mTableCell(align = MTableCellAlign.center) { +"${round(area.size)}" }
        mTableCell(align = MTableCellAlign.center, padding = MTableCellPadding.none) {
          mButton(
            getNodeTypeString(area.areaType),
            onClick = { handleShowMenuClick(it, area.id) }
          )
          mMenu(
            state.selectedMenuIndex == area.id,
            anchorElement = anchorElement,
            onClose = { _, reason -> handleOnClose() }) {
            mMenuItem(
              "Солидный",
              onClick = { handleSimpleClick(0) })
            mMenuItem(
              "Полусолидный",
              onClick = { handleSimpleClick(1) })
            mMenuItem("Матовое стекло", onClick = { handleSimpleClick(2) })
            mMenuItem("non-nodule", onClick = { handleSimpleClick(3) })
          }
        }
        mTableCell(align = MTableCellAlign.center, padding = MTableCellPadding.none) {
          mIconButton("close", onClick = { props.delete() })
        }
      }
      if (props.selected) {
        mTableRow {
          if (state.writing) {
            mTableCell(colSpan = 5) {
              commentInput()
            }
            mTableCell(align = MTableCellAlign.center, padding = MTableCellPadding.none) {
              mIconButton("done", onClick = {
                debugLog(state.comment)
                props.onCommentChanged(state.comment)
                setState { writing = false }
              })
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

  private fun StyledElementBuilder<MTableCellProps>.commentInput() {
    mTextField(
      value = state.comment,
      variant = MFormControlVariant.outlined,
      fullWidth = true,
      label = "Комментарий",
      onChange = ::onCommentChanged,
      margin = MFormControlMargin.none
    )
  }

  private fun StyledElementBuilder<MTableCellProps>.commentText() {
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

  private fun handleSimpleClick(type: Int) {
    props.changeType(type)
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

  private fun getNodeTypeString(type: AreaType): String {
    return when (type) {
      AreaType.NO_TYPE_NODULE -> "тип"
      AreaType.SOLID_NODULE -> "c"
      AreaType.PART_SOLID_NODULE -> "п"
      AreaType.PURE_SUBSOLID_NODULE -> "м"
      AreaType.NOT_ONKO -> "н.о."
    }
  }
}

interface ResearchTableRowProps : RProps {
  var onCommentChanged: (String) -> Unit
  var selected: Boolean
  var area: SelectedArea
  var select: () -> Unit
  var changeType: (Int) -> Unit
  var delete: () -> Unit
}

data class ResearchTableRowState(
  var selectedMenuIndex: Int = -1,
  var comment: String = "",
  var writing: Boolean
) : RState

fun RBuilder.researchTableRow(
  selected: Boolean,
  area: SelectedArea,
  select: () -> Unit,
  changeType: (Int) -> Unit,
  delete: () -> Unit,
  onCommentChanged: (String) -> Unit
) = child(ResearchTableRow::class) {
  attrs.select = select
  attrs.area = area
  attrs.selected = selected
  attrs.changeType = changeType
  attrs.delete = delete
  attrs.onCommentChanged = onCommentChanged
}