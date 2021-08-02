package components.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.list.ResearchList.Dependencies
import model.Research
import repository.MyResearchRepository

interface ResearchList {

  val models: Value<Model>

  fun reload()

  fun onItemClick(researchId: Int)

  data class Model(
    val items: List<Research>,
    val error: String,
    val loading: Boolean
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val researchRepository: MyResearchRepository
    val researchListOutput: Consumer<Output>
  }

  sealed class Output {
    data class ItemSelected(val researchId: Int) : Output()
  }
}

@Suppress("FunctionName") // Factory function
fun ResearchList(componentContext: ComponentContext, dependencies: Dependencies): ResearchList =
  ListResearchComponent(componentContext, dependencies)