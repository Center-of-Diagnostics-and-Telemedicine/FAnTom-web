package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.MarkModel
import model.Research
import model.ResearchSlicesSizesDataNew
import model.ExpertQuestionsModel
import repository.ExpertMarksRepository
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
    val expertMarksRepository: ExpertMarksRepository
  }

  sealed class Output {
    data class Marks(val models: List<ExpertQuestionsModel>) : Output()

    object CloseResearch : Output()
  }

  sealed class Input {
    data class AcceptMark(val mark: MarkModel) : Input()

    object Idle : Input()
    object CloseResearchRequested : Input()
  }
}