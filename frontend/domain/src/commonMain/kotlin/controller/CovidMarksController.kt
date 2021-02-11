package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Research
import model.ResearchSlicesSizesDataNew
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
    val marksOutput: (Output) -> Unit
    val research: Research
    val data: ResearchSlicesSizesDataNew
  }

  sealed class Output {
  }

  sealed class Input {
    object Idle : Input()
    object CloseResearchRequested : Input()
  }
}