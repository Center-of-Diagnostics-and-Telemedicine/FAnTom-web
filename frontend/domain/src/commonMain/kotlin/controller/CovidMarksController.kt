package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Circle
import model.Cut
import model.MarkModel
import model.ResearchSlicesSizesDataNew
import repository.MarksRepository
import view.CovidMarksView
import view.MarksView

interface CovidMarksController {

  val input: (Input) -> Unit

  fun onViewCreated(
    marksView: CovidMarksView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val marksRepository: MarksRepository
    val marksOutput: (Output) -> Unit
    val researchId: Int
    val data: ResearchSlicesSizesDataNew
  }

  sealed class Output {
  }

  sealed class Input {
    object Idle: Input()
    object CloseResearchRequested : Input()
  }
}