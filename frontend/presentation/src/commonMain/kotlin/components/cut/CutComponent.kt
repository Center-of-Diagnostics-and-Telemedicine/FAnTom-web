package components.cut

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import components.cutslider.Slider
import components.cut.Cut.Dependencies
import components.cut.Cut.Model

class CutComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies
) : Cut {
  override val model: Value<Model>
    get() = TODO("Not yet implemented")
  override val slider: Slider
    get() = TODO("Not yet implemented")
}