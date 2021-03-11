package research.covid

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemText
import com.ccfraser.muirwik.components.transitions.mCollapse
import model.LungLobeModel
import react.RBuilder
import react.dom.span

private val builder2 = RBuilder()

fun RBuilder.covidMarkView(
  model: LungLobeModel,
  handlePanelClick: () -> Unit,
  handleVariantClick: (Int) -> Unit,
  expand: Boolean,
  fullWidth: Boolean
) {
  val valueChosen = model.value != null
  mListItem(
    primaryText = if (fullWidth) model.fullName else model.shortName,
    secondaryText = if (valueChosen) model.value!!.description else "не выбрано",
    onClick = { handlePanelClick() },
    selected = valueChosen
  ) {
    if (expand) mIcon("expand_less") else mIcon("expand_more")
  }

  mCollapse(show = expand && fullWidth) {
    mList(disablePadding = true) {
      model.availableValues.forEach { lungLobeValue ->
        mListItem(
          button = true,
          onClick = { handleVariantClick(lungLobeValue.value) },
          selected = model.value == lungLobeValue
        ) {
          mListItemText(
            primary = builder2.span { +"${lungLobeValue.name}: ${lungLobeValue.description}" },
            inset = true
          )
        }
      }
    }
  }
}