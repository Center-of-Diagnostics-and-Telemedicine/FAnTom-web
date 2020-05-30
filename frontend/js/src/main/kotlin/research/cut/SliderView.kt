package research.cut

import com.ccfraser.muirwik.components.MSliderOrientation
import com.ccfraser.muirwik.components.MSliderValueLabelDisplay
import com.ccfraser.muirwik.components.mSlider
import react.RBuilder

fun RBuilder.slider(sliceNumber: Int, defaultValue: Int, maxValue: Int, onChange: (Int) -> Unit) {
  mSlider(
    value = sliceNumber,
    defaultValue = defaultValue,
    min = 1,
    max = maxValue,
    orientation = MSliderOrientation.horizontal,
    valueLabelDisplay = MSliderValueLabelDisplay.auto,
    onChange = { _, newValue -> onChange(newValue.toInt()) }
  )
}
