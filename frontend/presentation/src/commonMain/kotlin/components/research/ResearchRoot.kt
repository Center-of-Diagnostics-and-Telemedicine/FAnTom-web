package components.research

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.research.ResearchRoot.Dependencies
import components.researchmarks.ResearchMarks
import components.researchtools.ResearchTools
import repository.BrightnessRepository
import repository.MarksRepository
import repository.MipRepository
import repository.ResearchRepository

interface ResearchRoot {

  val toolsRouterState: Value<RouterState<*, Tools>>
  val marksRouterState: Value<RouterState<*, Marks>>

  val models: Value<Model>

  data class Model(
    val error: String,
    val loading: Boolean
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val researchRepository: ResearchRepository
    val marksRepository: MarksRepository
    val mipRepository: MipRepository
    val brightnessRepository: BrightnessRepository
    val researchOutput: Consumer<Output>
    val researchId: Int
  }

  data class Tools(val component: ResearchTools)
  data class Marks(val component: ResearchMarks)

  sealed class Output {
    object Back : Output()
  }
}

@Suppress("FunctionName") // Factory function
fun ResearchRoot(componentContext: ComponentContext, dependencies: Dependencies): ResearchRoot =
  ResearchRootComponent(
    componentContext = componentContext,
    dependencies = dependencies,
    tools = { childContext, output ->
      ResearchTools(
        componentContext = childContext,
        dependencies = object : ResearchTools.Dependencies, Dependencies by dependencies {
          override val toolsOutput: Consumer<ResearchTools.Output> = output
        }
      )
    },
    marks = { childContext, output ->
      ResearchMarks(
        componentContext = childContext,
        dependencies = object : ResearchMarks.Dependencies, Dependencies by dependencies {
          override val marksOutput: Consumer<ResearchMarks.Output> = output
        }
      )
    },
  )