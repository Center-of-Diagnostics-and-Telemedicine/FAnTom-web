package components.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import components.asValue
import components.getStore
import components.list.ResearchList.*
import store.list.ListStore.Intent

internal class ListResearchComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies
) : ResearchList, ComponentContext by componentContext, Dependencies by dependencies {

  private val store =
    instanceKeeper.getStore {
      ListStoreProvider(
        storeFactory = storeFactory,
        repository = researchRepository
      ).provide()
    }

  override val models: Value<Model> = store.asValue().map(stateToModel)

  override fun reload() {
    store.accept(Intent.ReloadRequested)
  }

  override fun onItemClick(researchId: Int) {
    researchListOutput.onNext(Output.ItemSelected(researchId))
  }
}