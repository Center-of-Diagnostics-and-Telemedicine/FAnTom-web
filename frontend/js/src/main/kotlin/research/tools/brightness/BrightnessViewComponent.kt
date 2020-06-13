package research.tools.brightness

import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.targetInputValue
import com.ccfraser.muirwik.components.transitions.mCollapse
import kotlinx.html.InputType
import react.RBuilder
import research.tools.ToolsComponent
import styled.css
import view.BrightnessView

fun RBuilder.renderBrightness(
  onGammaChange: (Double) -> Unit,
  onWhiteChange: (Int) -> Unit,
  onBlackChange: (Int) -> Unit,
  model: BrightnessView.Model
) {
  mCollapse(show = true) {
    mList {
      css(ToolsComponent.ToolsStyles.nested)
      mListItem {
        white(model.whiteValue, onWhiteChange)
      }
      mListItem {
        black(model.blackValue, onBlackChange)
      }
      mListItem {
        gamma(model.gammaValue, onGammaChange)
      }
    }
  }
}

fun RBuilder.white(whiteValue: Int, whiteChanged: (Int) -> Unit) {
  mTextField(
    label = "Белый",
    type = InputType.number,
    value = whiteValue.toString(),
    onChange = { val value = it.targetInputValue; whiteChanged(value.toInt()) }
  )
}

fun RBuilder.black(blackValue: Int, blackChanged: (Int) -> Unit) {
  mTextField(
    label = "Черный",
    type = InputType.number,
    value = blackValue.toString(),
    onChange = { val value = it.targetInputValue; blackChanged(value.toInt()) }
  )
}

fun RBuilder.gamma(gammaValue: Double, gammaChanged: (Double) -> Unit) {
  mTextField(
    label = "Гамма",
    type = InputType.number,
    value = gammaValue.toString(),
    onChange = { val value = it.targetInputValue; gammaChanged(value.toDouble()) }
  )
}
