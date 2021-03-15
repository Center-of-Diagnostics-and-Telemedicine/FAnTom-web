package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Research
import model.ResearchSlicesSizesDataNew
import repository.ExpertMarksRepository
import repository.MarksRepository
import view.ExpertMarksView

interface ExpertMarksController {

  val input: (Input) -> Unit

  fun onViewCreated(
    marksView: ExpertMarksView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val expertMarksOutput: (Output) -> Unit
    val research: Research
    val data: ResearchSlicesSizesDataNew
    val marksRepository: MarksRepository
    val expertMarksRepository: ExpertMarksRepository
  }

  sealed class Output {
    object CloseResearch : Output()
  }

  sealed class Input {
    object Idle : Input()
    object CloseResearchRequested : Input()
  }
}