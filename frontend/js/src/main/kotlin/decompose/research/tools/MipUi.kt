package decompose.research.tools

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.form.mFormControlLabel
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.transitions.mCollapse
import components.mip.Mip
import components.mip.Mip.Model
import decompose.Props
import decompose.RenderableComponent
import decompose.research.tools.MipUi.State
import kotlinx.css.Display
import kotlinx.css.display
import kotlinx.css.paddingLeft
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RState
import react.setState
import styled.css
import styled.styledDiv

class MipUi(props: Props<Mip>) : RenderableComponent<Mip, State>(
  props = props,
  initialState = State(
    model = props.component.model.value,
    open = true
  )
) {

  private val altBuilder = RBuilder()

  init {
    component.model.bindToState { model = it }
  }

  override fun RBuilder.render() {
    styledDiv {
      attrs.onClickFunction = { setState { open = !state.open } }
      mCollapse(show = state.open) {
        css { paddingLeft = 4.spacingUnits }
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
      value = state.model.currentValue ?: 0,
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