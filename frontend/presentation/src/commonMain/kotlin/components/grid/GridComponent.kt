package components.grid

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.observe
import com.arkivanov.decompose.value.operator.map
import components.asValue
import components.getStore
import components.grid.Grid.Dependencies
import components.grid.Grid.Model
import model.GridType
import store.tools.MyGridStore.Intent

class GridComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
) : Grid, ComponentContext by componentContext, Dependencies by dependencies {

  private val store = instanceKeeper.getStore {
    GridStoreProvider(
      storeFactory = storeFactory,
      researchId = researchId,
      data = data,
      gridRepository = gridRepository
    ).provide()
  }

  override val model: Value<Model> = store.asValue().map(stateToModel)

  override fun changeGrid(gridType: GridType) {
    store.accept(Intent.ChangeGrid(gridType))
  }
}