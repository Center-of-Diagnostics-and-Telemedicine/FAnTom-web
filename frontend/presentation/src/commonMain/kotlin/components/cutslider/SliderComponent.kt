package components.cutslider

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import components.asValue
import components.cutslider.Slider.Dependencies
import components.getStore
import store.slider.SliderStore

class SliderComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
) : Slider, ComponentContext by componentContext, Dependencies by dependencies {

  private val store = instanceKeeper.getStore {
    SliderStoreProvider(
      storeFactory = storeFactory,
      researchId = researchId,
      plane = plane
    ).provide()
  }

  override val model: Value<Slider.Model> = store.asValue().map(stateToModel)

  override fun onValueChange(value: Int) {
    store.accept(SliderStore.Intent.HandleChange(value))
    sliderOutput.onNext(Slider.Output.SliceNumberChanged(value))
  }
}