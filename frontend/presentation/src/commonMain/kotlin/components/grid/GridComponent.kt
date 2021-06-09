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
      data = data
    ).provide()
  }

  override val model: Value<Model> = store.asValue().map(stateToModel)

  init {
    store.asValue().observe(lifecycle) {
      println("GridComponent store.asValue().observe(lifecycle) called")
    }
  }

  override fun changeGrid(gridType: GridType) {
    println("changeGrid called")
    store.accept(Intent.ChangeGrid(gridType))
  }
}