package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import repository.ResearchRepository
import view.ResearchView

interface ResearchController {

  fun onViewCreated(
    researchView: ResearchView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val researchRepository: ResearchRepository
    val researchId: Int
  }

  sealed class Output {
    data class ItemSelected(val id: Int) : Output()
  }
}
