package components.research

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.research.Research.Dependencies
import repository.ResearchRepository

interface Research {

  val models: Value<Model>

  data class Model(
    val error: String,
    val loading: Boolean
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val researchRepository: ResearchRepository
    val researchOutput: Consumer<Output>
    val researchId: Int
  }

  sealed class Output {
  }
}

@Suppress("FunctionName") // Factory function
fun Research(componentContext: ComponentContext, dependencies: Dependencies): Research =
  ResearchComponent(componentContext, dependencies)