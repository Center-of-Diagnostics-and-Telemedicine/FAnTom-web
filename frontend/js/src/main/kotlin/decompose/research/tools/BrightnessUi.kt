package decompose.research.tools

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.transitions.mCollapse
import components.brightness.Brightness
import decompose.RenderableComponent
import decompose.research.tools.BrightnessUi.State
import kotlinx.html.InputType
import react.RBuilder
import react.RState
import research.tools.ToolsComponent
import styled.css

class BrightnessUi(props: Props<Brightness>) : RenderableComponent<Brightness, State>(
  props = props,
  initialState = State(
    model = props.component.model.value,
    open = false
  )
) {

  init {
    component.model.bindToState { model = it }
  }

  override fun RBuilder.render() {
    mCollapse(show = state.open) {
      mList {
        css(ToolsComponent.ToolsStyles.nested)
        mListItem {
          white(state.model.whiteValue, component::onWhiteChanged)
        }
        mListItem {
          black(state.model.blackValue, component::onBlackChanged)
        }
        mListItem {
          gamma(state.model.gammaValue, component::onGammaChanged)
        }
      }
    }
  }

  private fun RBuilder.white(whiteValue: Int, whiteChanged: (Int) -> Unit) {
    mTextField(
      label = "Белый",
      type = InputType.number,
      value = whiteValue.toString(),
      onChange = { val value = it.targetInputValue; whiteChanged(value.toInt()) }
    )
  }

  private fun RBuilder.black(blackValue: Int, blackChanged: (Int) -> Unit) {
    mTextField(
      label = "Черный",
      type = InputType.number,
      value = blackValue.toString(),
      onChange = { val value = it.targetInputValue; blackChanged(value.toInt()) }
    )
  }

  private fun RBuilder.gamma(gammaValue: Double, gammaChanged: (Double) -> Unit) {
    mTextField(
      label = "Гамма",
      type = InputType.number,
      value = gammaValue.toString(),
      onChange = { val value = it.targetInputValue; gammaChanged(value.toDouble()) }
    )
  }

  class State(
    var model: Brightness.Model,
    var open: Boolean
  ) : RState
}