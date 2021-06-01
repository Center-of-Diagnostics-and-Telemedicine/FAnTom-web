package components.mainframe

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.listroot.ListRoot
import components.mainframe.MainFrame.Dependencies
import components.research.Research
import repository.LoginRepository
import repository.ResearchRepository

interface MainFrame {

  val routerState: Value<RouterState<*, Child>>

  sealed class Child {
    data class List(val component: ListRoot) : Child()
    data class Research(val component: components.research.Research) : Child()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val repository: LoginRepository
    val researchRepository: ResearchRepository
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
    research = { childContext, researchId, output ->
      Research(
        componentContext = childContext,
        dependencies = object : Research.Dependencies, Dependencies by dependencies {
          override val researchOutput: Consumer<Research.Output> = output
          override val researchId: Int = researchId
        }
      )
    },
  )