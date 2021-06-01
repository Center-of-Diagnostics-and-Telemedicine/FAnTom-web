package components.mip

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import components.asValue
import components.getStore
import components.mip.Mip.Dependencies
import store.tools.MipStore
import store.tools.MipStore.Intent

internal class MipComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies
) : Mip, ComponentContext by componentContext, Dependencies by dependencies {

  private val store: MipStore = instanceKeeper.getStore {
    MipStoreProvider(
      storeFactory = storeFactory,
      mipRepository = mipRepository,
      researchId = researchId
    ).provide()
  }

  override val model: Value<Mip.Model> = store.asValue().map(stateToModel)

  override fun onItemClick(mip: model.Mip) {
    store.accept(Intent.HandleMipClick(mip))
  }

  override fun onMipValueChanged(value: Int) {
    store.accept(Intent.HandleMipValueChanged(value))
  }

}