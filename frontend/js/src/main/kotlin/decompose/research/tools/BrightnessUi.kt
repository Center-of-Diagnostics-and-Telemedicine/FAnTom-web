package decompose.research.tools

import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.spacingUnits
import com.ccfraser.muirwik.components.targetInputValue
import com.ccfraser.muirwik.components.transitions.mCollapse
import components.brightness.Brightness
import decompose.Props
import decompose.RenderableComponent
import decompose.research.tools.BrightnessUi.State
import kotlinx.css.paddingLeft
import kotlinx.html.InputType
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.RBuilder
import react.RState
import react.setState
import styled.css
import styled.styledDiv

class BrightnessUi(props: Props<Brightness>) : RenderableComponent<Brightness, State>(
  props = props,
  initialState = State(
    model = props.component.model.value,
    open = true
  )
) {

  init {
    component.model.bindToState { model = it }
  }

  override fun RBuilder.render() {
    styledDiv {
      attrs.onClickFunction = { setState { open = !state.open } }
      mCollapse(show = state.open) {
        mList {
          css { paddingLeft = 4.spacingUnits }
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

  private fun changeVisibility(event: Event) = setState { open = !open }

  class State(
    var model: Brightness.Model,
    var open: Boolean
  ) : RState
}