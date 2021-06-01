package components.brightness

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import components.asValue
import components.brightness.Brightness.Dependencies
import components.brightness.Brightness.Model
import components.getStore
import store.tools.BrightnessStore
import store.tools.BrightnessStore.Intent

internal class BrightnessComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies
) : Brightness, ComponentContext by componentContext, Dependencies by dependencies {

  private val store: BrightnessStore = instanceKeeper.getStore {
    BrightnessStoreProvider(
      storeFactory = storeFactory,
      brightnessRepository = brightnessRepository,
      researchId = researchId
    ).provide()
  }

  override val model: Value<Model> = store.asValue().map(stateToModel)

  override fun onBlackChanged(value: Int) {
    store.accept(Intent.HandleBlackChanged(value))
  }

  override fun onWhiteChanged(value: Int) {
    store.accept(Intent.HandleWhiteValueChanged(value))
  }

  override fun onGammaChanged(value: Double) {
    store.accept(Intent.HandleGammaValueChanged(value))
  }

}