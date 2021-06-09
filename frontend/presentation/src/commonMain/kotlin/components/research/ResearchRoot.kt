package components.research

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cutscontainer.CutsContainer
import components.research.ResearchRoot.Dependencies
import components.researchmarks.ResearchMarks
import components.researchtools.ResearchTools
import model.ResearchData
import repository.*

interface ResearchRoot {

  val toolsRouterState: Value<RouterState<*, ToolsChild>>
  val marksRouterState: Value<RouterState<*, MarksChild>>
  val cutsContainerRouterState: Value<RouterState<*, CutsChild>>

  val models: Value<Model>

  data class Model(
    val error: String,
    val loading: Boolean,
    val data: ResearchData?
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val researchRepository: ResearchRepository
    val marksRepository: MarksRepository
    val mipRepository: MipRepository
    val brightnessRepository: BrightnessRepository
    val gridRepository: GridRepository
    val researchOutput: Consumer<Output>
    val researchId: Int
  }

  sealed class ToolsChild {
    data class Data(val component: ResearchTools) : ToolsChild()
    object None : ToolsChild()
  }

  sealed class MarksChild {
    data class Data(val component: ResearchMarks) : MarksChild()
    object None : MarksChild()
  }

  sealed class CutsChild {
    data class Data(val component: CutsContainer) : CutsChild()
    object None : CutsChild()
  }

  sealed class Output {
    object Back : Output()
  }
}

@Suppress("FunctionName") // Factory function
fun ResearchRoot(componentContext: ComponentContext, dependencies: Dependencies): ResearchRoot =
  ResearchRootComponent(
    componentContext = componentContext,
    dependencies = dependencies,
    tools = { childContext, researchData, output ->
      ResearchTools(
        componentContext = childContext,
        dependencies = object : ResearchTools.Dependencies, Dependencies by dependencies {
          override val toolsOutput: Consumer<ResearchTools.Output> = output
          override val data: ResearchData = researchData
        }
      )
    },
    marks = { childContext, researchData, output ->
      ResearchMarks(
        componentContext = childContext,
        dependencies = object : ResearchMarks.Dependencies, Dependencies by dependencies {
          override val marksOutput: Consumer<ResearchMarks.Output> = output
          override val data: ResearchData = researchData
        }
      )
    },
    cuts = { childContext, researchData, output ->
      CutsContainer(
        componentContext = childContext,
        dependencies = object : CutsContainer.Dependencies, Dependencies by dependencies {
          override val cutsContainerOutput: Consumer<CutsContainer.Output> = output
          override val data: ResearchData = researchData
        }
      )

    }
  )