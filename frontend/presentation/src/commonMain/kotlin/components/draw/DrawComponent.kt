package components.draw

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import components.asValue
import components.draw.Draw.Dependencies
import components.draw.Draw.Model
import components.getStore

class DrawComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies
) : Draw, ComponentContext by componentContext, Dependencies by dependencies {

  private val store = instanceKeeper.getStore {
    DrawStoreProvider(
      storeFactory = storeFactory,
      researchId = researchId,
      cutType = cutType
    ).provide()
  }

  override val model: Value<Model> = store.asValue().map(stateToModel)
}