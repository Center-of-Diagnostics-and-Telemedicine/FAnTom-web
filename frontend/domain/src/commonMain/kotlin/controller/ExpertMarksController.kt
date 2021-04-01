package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.MarkModel
import model.Research
import model.ResearchSlicesSizesDataNew
import model.RoiExpertQuestionsModel
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
    data class Marks(val models: List<RoiExpertQuestionsModel>) : Output()

    object CloseResearch : Output()
  }

  sealed class Input {
    data class AcceptMark(val mark: MarkModel) : Input()

    object Idle : Input()
    object CloseResearchRequested : Input()
  }
}