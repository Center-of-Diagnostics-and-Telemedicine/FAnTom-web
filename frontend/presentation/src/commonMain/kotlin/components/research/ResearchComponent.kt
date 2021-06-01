package components.research

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import components.asValue
import components.getStore

internal class ResearchComponent(
  componentContext: ComponentContext,
  dependencies: Research.Dependencies
) : Research, ComponentContext by componentContext, Research.Dependencies by dependencies {

  private val store =
    instanceKeeper.getStore {
      ResearchComponentStoreProvider(
        storeFactory = storeFactory,
        repository = researchRepository,
        researchId = researchId
      ).provide()
    }

  override val models: Value<Research.Model> = store.asValue().map(stateToModel)

//  override fun reload() {
//    store.accept(ResearchStore.Intent.ReloadRequested)
//  }
//
//  override fun onItemClick(researchId: Int) {
//    listOutput.onNext(Research.Output.ItemSelected(researchId))
//  }
}