package research.marks

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemIcon
import com.ccfraser.muirwik.components.list.mListItemText
import kotlinx.css.padding
import model.MarkDomain
import org.w3c.dom.Node
import react.*
import react.dom.findDOMNode
import react.dom.span
import styled.css
import styled.styledDiv
import view.MarksView

class MarkItemView(prps: MarkItemProps) : RComponent<MarkItemProps, MarkItemState>(prps) {

  private val builder2 = RBuilder()
  private var infoRef: Node? = null

  override fun RBuilder.render() {
    mListItem {
      ref {
        infoRef = findDOMNode(it)
      }
      attrs {
        onMouseEnter = { openInfo() }
        onMouseLeave = { closeInfo() }
      }
      mListItemIcon {
        mIcon("fiber_manual_record")
      }
      mListItemText(primary = builder2.span { +props.mark.markData.name() })
    }
    mPopover(state.showInfo, onClose = { _, _ -> closeInfo() }) {
      attrs.apply {
        anchorReference = MPopoverAnchorRef.anchorEl
        anchorEl = infoRef
        anchorOriginVertical = MPopoverVerticalPosition.top
        anchorOriginHorizontal = MPopoverHorizontalPosition.left
        transformOriginVertical = MPopoverVerticalPosition.top
        transformOriginHorizontal = MPopoverHorizontalPosition.right
      }
      styledDiv {
        css {
          padding(2.spacingUnits)
        }
        +props.mark.markData.name()
      }
    }
  }

  private fun openInfo() = setState { showInfo = true }
  private fun closeInfo() = setState { showInfo = false }

}

class MarkItemState(
  var showInfo: Boolean,
) : RState

interface MarkItemProps : RProps {
  var mark: MarkDomain
  var eventOutput: (MarksView.Event) -> Unit
}

fun RBuilder.markView(
  mark: MarkDomain, eventOutput:
  (MarksView.Event) -> Unit
) = child(MarkItemView::class) {
  attrs.mark = mark
  attrs.eventOutput = eventOutput
}
