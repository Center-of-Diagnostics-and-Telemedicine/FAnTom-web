package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Circle
import model.Cut
import model.MarkModel
import model.ResearchSlicesSizesDataNew
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
    val researchId: Int
    val data: ResearchSlicesSizesDataNew
  }

  sealed class Output {
    data class Marks(val list: List<MarkModel>) : Output()
  }

  sealed class Input {
    data class AddNewMark(val circle: Circle, val sliceNumber: Int, val cut: Cut) : Input()
    data class SelectMark(val mark: MarkModel) : Input()
    data class UnselectMark(val mark: MarkModel) : Input()
    data class UpdateMark(val markToUpdate: MarkModel) : Input()
    data class UpdateMarkWithSave(val mark: MarkModel) : Input()
    object Idle: Input()
    object DeleteClick : Input()
  }
}
