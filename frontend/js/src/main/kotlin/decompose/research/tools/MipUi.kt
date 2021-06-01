package decompose.research.tools

import com.ccfraser.muirwik.components.MSliderValueLabelDisplay
import com.ccfraser.muirwik.components.form.mFormControlLabel
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.mRadio
import com.ccfraser.muirwik.components.mRadioGroup
import com.ccfraser.muirwik.components.mSlider
import com.ccfraser.muirwik.components.transitions.mCollapse
import components.mip.Mip
import components.mip.Mip.Model
import decompose.RenderableComponent
import decompose.research.tools.MipUi.State
import kotlinx.css.Display
import kotlinx.css.display
import react.RBuilder
import react.RState
import research.tools.ToolsComponent
import styled.css

class MipUi(props: Props<Mip>) : RenderableComponent<Mip, State>(
  props = props,
  initialState = State(
    model = props.component.model.value,
    open = false
  )
) {

  private val altBuilder = RBuilder()

  init {
    component.model.bindToState { model = it }
  }

  override fun RBuilder.render() {
    mCollapse(show = state.open) {
      css(ToolsComponent.ToolsStyles.nested)
      mList {
        mListItem {
          mip()
        }
        mListItem {
          mipValue()
        }
      }
    }
  }

  private fun RBuilder.mip() {
    mRadioGroup(
      value = state.model.current.valueName,
      onChange = { _, value -> component.onItemClick(model.Mip.build(value)) }) {
      css { display = Display.inlineFlex }
      state.model.items.forEach { mip ->
        mFormControlLabel(
          label = mip.name,
          value = mip.valueName,
          control = altBuilder.mRadio()
        )
      }
    }
  }

  private fun RBuilder.mipValue() {
    mSlider(
      value = state.model.currentValue,
      max = 10,
      min = 0,
      valueLabelDisplay = MSliderValueLabelDisplay.auto,
      showMarks = true,
      step = 1,
      onChange = { _, newValue -> component.onMipValueChanged(newValue.toInt()) }
    )
  }

  class State(
    var model: Model,
    var open: Boolean
  ) : RState
}