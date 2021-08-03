package controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Research
import repository.ResearchRepository
import view.CategoryView
import view.FilterView
import view.ListView

interface ListController {

  fun onViewCreated(
    listView: ListView,
    filterView: FilterView,
    categoryView: CategoryView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val researchRepository: ResearchRepository
    val listOutput: (Output) -> Unit
  }

  sealed class Output {
    data class ItemSelected(val research: Research) : Output()
  }
}
