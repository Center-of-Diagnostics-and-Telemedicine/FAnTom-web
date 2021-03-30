package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Research
import model.ResearchSlicesSizesDataNew
import repository.ExpertMarksRepository
import repository.ExpertRoiRepository
import repository.MarksRepository
import view.ExpertMarksView

interface ExpertMarksController {

  val input: (Input) -> Unit

  fun onViewCreated(
    expertMarksView: ExpertMarksView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val expertMarksOutput: (Output) -> Unit
    val research: Research
    val data: ResearchSlicesSizesDataNew
    val expertRoiRepository: ExpertRoiRepository
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