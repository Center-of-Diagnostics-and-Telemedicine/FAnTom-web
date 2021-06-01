package components.listfilters

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.listfilters.ListFilters.Dependencies
import model.Filter
import repository.ResearchRepository

interface ListFilters {

  val models: Value<Model>

  fun onItemClick(filter: Filter)

  data class Model(
    val items: List<Filter>,
    val current: Filter
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val listFiltersOutput: Consumer<Output>
  }

  sealed class Output {
    data class ItemSelected(val researchId: Int) : Output()
  }
}

@Suppress("FunctionName") // Factory function
fun ListFilters(componentContext: ComponentContext, dependencies: Dependencies): ListFilters =
  ListFiltersComponent(componentContext, dependencies)