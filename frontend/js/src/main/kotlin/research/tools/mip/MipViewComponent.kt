package research.tools.mip

import com.ccfraser.muirwik.components.MSliderValueLabelDisplay
import com.ccfraser.muirwik.components.form.mFormControlLabel
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.mRadio
import com.ccfraser.muirwik.components.mRadioGroup
import com.ccfraser.muirwik.components.mSlider
import com.ccfraser.muirwik.components.transitions.mCollapse
import kotlinx.css.Display
import kotlinx.css.display
import model.Mip
import react.RBuilder
import research.tools.ToolsComponent
import styled.css
import view.MipView

val altBuilder = RBuilder()

fun RBuilder.renderMip(
  model: MipView.Model,
  onClick: (Mip) -> Unit,
  onChange: (Int) -> Unit
) {
  mCollapse(show = true) {
    css(ToolsComponent.ToolsStyles.nested)
    mList {
      mListItem {
        mip(
          items = model.items,
          currentMip = model.current,
          onClick = onClick
        )
      }
      model.currentValue?.let { currentValue ->
        mListItem {
          mipValue(
            currentValue = currentValue,
            onChange = onChange
          )
        }
      }
    }
  }
}

fun RBuilder.mip(
  items: List<Mip>,
  currentMip: Mip,
  onClick: (Mip) -> Unit
) {
  mRadioGroup(
    value = currentMip.valueName,
    onChange = { _, value -> onClick(Mip.build(value)) }) {
    css { display = Display.inlineFlex }
    items.forEach { mip ->
      mFormControlLabel(
        label = mip.name,
        value = mip.valueName,
        control = altBuilder.mRadio()
      )
    }
  }
}

fun RBuilder.mipValue(currentValue: Int, onChange: (Int) -> Unit) {
  mSlider(
    value = currentValue,
    max = 10,
    min = 0,
    valueLabelDisplay = MSliderValueLabelDisplay.auto,
    showMarks = true,
    step = 1,
    onChange = { _, newValue -> onChange(newValue.toInt()) }
  )
}

