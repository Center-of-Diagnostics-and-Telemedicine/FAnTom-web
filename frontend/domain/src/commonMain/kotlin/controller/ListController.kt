package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
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

  sealed class Output {
    data class ItemSelected(val id: Int) : Output()
  }
}
