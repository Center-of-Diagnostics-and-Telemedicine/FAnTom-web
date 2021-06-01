package components.listfilters

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import components.asValue
import components.getStore
import components.listfilters.ListFilters.Dependencies
import components.listfilters.ListFilters.Model
import model.Filter
import store.list.FilterStore.Intent

internal class ListFiltersComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies
) : ListFilters, ComponentContext by componentContext, Dependencies by dependencies {

  private val store =
    instanceKeeper.getStore {
      ListFiltersComponentStoreProvider(
        storeFactory = storeFactory,
      ).provide()
    }

  override val models: Value<Model> = store.asValue().map(stateToModel)

  override fun onItemClick(filter: Filter) {
    store.accept(Intent.HandleFilterClick(filter))
  }
}