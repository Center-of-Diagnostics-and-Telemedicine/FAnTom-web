package components.cutslider

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import components.asValue
import components.cutslider.Slider.Dependencies
import components.getStore

class SliderComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies
) : Slider, ComponentContext by componentContext, Dependencies by dependencies {

  private val store = instanceKeeper.getStore {
    SliderStoreProvider(
      storeFactory = storeFactory,
      researchId = researchId
    ).provide()
  }

  override val model: Value<Slider.Model> = store.asValue().map(stateToModel)

  override fun onValueChange(value: Int) {
    TODO("Not yet implemented")
  }
}