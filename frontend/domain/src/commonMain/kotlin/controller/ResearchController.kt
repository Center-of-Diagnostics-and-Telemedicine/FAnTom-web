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
    val researchOutput: (Output) -> Unit
  }

  sealed class Output {
    object Close : Output()
  }
}
