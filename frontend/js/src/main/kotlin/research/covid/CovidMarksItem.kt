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

//fun RBuilder.covidMarkView(model: LungLobeModel, handleChange: (Int) -> Unit) {
//  val htmlId = "${model.shortName}-input"
//  mInputLabel(caption = model.shortName, htmlFor = htmlId)
//
//  // Method 1, using input props
//  val inputProps: RProps = jsObject { }
//  inputProps.asDynamic().name = htmlId
//  inputProps.asDynamic().id = htmlId
//  mTooltip(title = model.fullName, placement = TooltipPlacement.left) {
//    mSelect(
//      model.value?.value ?: 0,
//      name = "left",
//      variant = MFormControlVariant.outlined,
//      onChange = { event, _ -> handleChange(event.targetValue.toString().toInt()) }) {
//      attrs.inputProps = inputProps
//      model.availableValues.forEach {
//        mTooltip(title = it.description, placement = TooltipPlacement.left) {
//          mMenuItem(primaryText = it.name, value = it.value.toString())
//        }
//      }
//    }
//  }
//}
//
//private fun handleLeft(event: Event) {
//  val value = event.targetValue
//}