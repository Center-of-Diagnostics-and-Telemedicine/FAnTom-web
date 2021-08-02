package components.mainframe

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.listroot.ListRoot
import components.mainframe.MainFrame.Dependencies
import components.research.ResearchRoot
import repository.*

interface MainFrame {

  val routerState: Value<RouterState<*, Child>>

  sealed class Child {
    data class List(val component: ListRoot) : Child()
    data class Research(val component: components.research.ResearchRoot) : Child()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val loginRepository: LoginRepository
    val researchRepository: MyResearchRepository
    val brightnessRepository: MyBrightnessRepository
    val mipRepository: MyMipRepository
    val gridRepository: GridRepository
    val marksRepository: MarksRepository
    val mainFrameOutput: Consumer<Output>
  }

  sealed class Output {
    object Unauthorized : Output()
  }
}

@Suppress("FunctionName") // Factory function
fun MainFrame(componentContext: ComponentContext, dependencies: Dependencies): MainFrame =
  MainFrameComponent(
    componentContext = componentContext,
    dependencies = dependencies,
    list = { childContext, output ->
      ListRoot(
        componentContext = childContext,
        dependencies = object : ListRoot.Dependencies, Dependencies by dependencies {
          override val listRootOutput: Consumer<ListRoot.Output> = output
        }
      )
    },
    researchRoot = { childContext, researchId, output ->
      ResearchRoot(
        componentContext = childContext,
        dependencies = object : ResearchRoot.Dependencies, Dependencies by dependencies {
          override val researchOutput: Consumer<ResearchRoot.Output> = output
          override val researchId: Int = researchId
        }
      )
    },
  )