package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.*
import repository.MarksRepository
import view.MarksView

interface MarksController {

  val input: (Input) -> Unit

  fun onViewCreated(
    marksView: MarksView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val marksRepository: MarksRepository
    val marksOutput: (Output) -> Unit
    val research: Research
    val data: ResearchSlicesSizesDataNew
  }

  sealed class Output {
    data class Marks(val list: List<MarkModel>) : Output()
    data class CenterSelectedMark(val mark: MarkModel) : Output()
    data class AcceptMark(val mark: MarkModel) : Output()

    object CloseResearch : Output()
  }

  sealed class Input {
    data class AddNewMark(val shape: Shape, val sliceNumber: Int, val cut: Cut) : Input()
    data class SelectMark(val mark: MarkModel) : Input()
    data class UnselectMark(val mark: MarkModel) : Input()
    data class UpdateMarkWithoutSave(val markToUpdate: MarkModel) : Input()
    data class UpdateMarkWithSave(val mark: MarkModel) : Input()
    object Idle: Input()
    object DeleteClick : Input()
    object CloseResearchRequested : Input()
  }
}
