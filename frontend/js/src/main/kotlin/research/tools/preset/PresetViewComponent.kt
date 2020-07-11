package research.tools.preset

import com.ccfraser.muirwik.components.form.mFormControlLabel
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.mRadio
import com.ccfraser.muirwik.components.mRadioGroup
import com.ccfraser.muirwik.components.transitions.mCollapse
import kotlinx.css.Display
import kotlinx.css.display
import model.Presets
import react.RBuilder
import research.tools.ToolsComponent
import styled.css
import view.PresetView

private val altBuilder = RBuilder()

fun RBuilder.renderPreset(
  model: PresetView.Model,
  onChange: (Presets) -> Unit
) {
  mCollapse(show = true) {
    css(ToolsComponent.ToolsStyles.nested)
    mList {
      mListItem {
        mRadioGroup(
          value = model.current.valueName,
          onChange = { _, value -> onChange(Presets.build(value)) }) {
          css { display = Display.inlineFlex }
          model.items.forEach { preset ->
            mFormControlLabel(
              preset.name,
              value = preset.valueName,
              control = altBuilder.mRadio()
            )
          }
        }
      }
    }
  }
}
