package components.shapes

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import components.asValue
import components.getStore
import components.shapes.Shapes.Dependencies
import components.shapes.Shapes.Model

class ShapesComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies
) : Shapes, ComponentContext by componentContext, Dependencies by dependencies {

  private val store = instanceKeeper.getStore {
    ShapesStoreProvider(
      storeFactory = storeFactory,
      researchId = researchId,
      plane = plane
    ).provide()
  }

  override val model: Value<Model> = store.asValue().map(stateToModel)
}