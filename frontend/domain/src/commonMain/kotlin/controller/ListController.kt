package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Filter
import repository.ResearchRepository
import view.FilterView
import view.ListView

interface ListController {

  fun onViewCreated(
    listView: ListView,
    filterView: FilterView,
    viewLifecycle: Lifecycle,
    output: (Output) -> Unit
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val researchRepository: ResearchRepository
  }

  sealed class Input {
    data class FilterChanged(val filter: Filter) : Input()
  }

  sealed class Output {
    data class ItemSelected(val id: String) : Output()
  }
}
