package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Circle
import model.Cut
import model.MarkDomain
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
  }

  sealed class Output {
    data class Marks(val list: List<MarkDomain>) : Output()
  }

  sealed class Input {
    data class AddNewMark(val circle: Circle, val sliceNumber: Int, val cut: Cut) : Input()
    data class SelectMark(val mark: MarkDomain) : Input()
    data class UnselectMark(val mark: MarkDomain) : Input()
    data class UpdateMark(val markToUpdate: MarkDomain) : Input()
    data class UpdateMarkWithSave(val mark: MarkDomain) : Input()
    object Idle: Input()
    object DeleteClick : Input()
  }
}
