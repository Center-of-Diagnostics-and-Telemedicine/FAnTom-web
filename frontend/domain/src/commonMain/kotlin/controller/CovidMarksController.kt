package controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Research
import model.ResearchData
import repository.CovidMarksRepository
import view.CovidMarksView

interface CovidMarksController {

  val input: (Input) -> Unit

  fun onViewCreated(
    marksView: CovidMarksView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val covidMarksRepository: CovidMarksRepository
    val covidMarksOutput: (Output) -> Unit
    val research: Research
    val data: ResearchData
  }

  sealed class Output {
    object CloseResearch : Output()
  }

  sealed class Input {
    object Idle : Input()
    object CloseResearchRequested : Input()
  }
}