package components.listroot

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.list.ResearchList
import components.listfilters.ListFilters
import components.listroot.ListRoot.Dependencies
import repository.LoginRepository
import repository.ResearchRepository

interface ListRoot {

  val filtersRouterState: Value<RouterState<*, Filters>>
  val listRouterState: Value<RouterState<*, List>>

  data class Model(
    val filtersOpen: Boolean
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val repository: LoginRepository
    val researchRepository: ResearchRepository
    val listRootOutput: Consumer<Output>
  }

  data class Filters(val component: ListFilters)
  data class List(val component: ResearchList)

  sealed class Output {
    data class NavigateToResearch(val researchId: Int) : Output()
  }
}

@Suppress("FunctionName") // Factory function
fun ListRoot(componentContext: ComponentContext, dependencies: Dependencies): ListRoot =
  ListRootComponent(
    componentContext = componentContext,
    dependencies = dependencies,
    filter = { childContext, output ->
      ListFilters(
        componentContext = childContext,
        dependencies = object : ListFilters.Dependencies, Dependencies by dependencies {
          override val listFiltersOutput: Consumer<ListFilters.Output> = output
        }
      )
    },
    list = { childContext, output ->
      ResearchList(
        componentContext = childContext,
        dependencies = object : ResearchList.Dependencies, Dependencies by dependencies {
          override val researchListOutput: Consumer<ResearchList.Output> = output
        }
      )
    },
  )