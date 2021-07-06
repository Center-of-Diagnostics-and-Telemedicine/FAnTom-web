package components.cut

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import components.asValue
import components.cut.Cut.Dependencies
import components.getStore


class CutComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies
) : Cut, ComponentContext by componentContext, Dependencies by dependencies {

  val store = instanceKeeper.getStore {
    MyCutStoreProvider(
      storeFactory = storeFactory,
      brightnessRepository = brightnessRepository,
      mipRepository = mipRepository,
      researchId = researchId,
      researchRepository = researchRepository,
      plane = plane
    ).provide()
  }

  override val model: Value<Cut.Model> = store.asValue().map(stateToModel)

}