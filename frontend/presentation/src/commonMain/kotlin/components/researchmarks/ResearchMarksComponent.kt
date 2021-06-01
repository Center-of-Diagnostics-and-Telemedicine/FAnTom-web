package components.researchmarks

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import components.asValue
import components.getStore
import components.researchmarks.ResearchMarks.Dependencies
import components.researchmarks.ResearchMarks.Model

internal class ResearchMarksComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies
) : ResearchMarks, ComponentContext by componentContext, Dependencies by dependencies {

  private val store =
    instanceKeeper.getStore {
      ResearchMarksStoreProvider(
        storeFactory = storeFactory,
        researchRepository = researchRepository,
        marksRepository = marksRepository,
        researchId = researchId
      ).provide()
    }

  override val models: Value<Model> = store.asValue().map(stateToModel)
}