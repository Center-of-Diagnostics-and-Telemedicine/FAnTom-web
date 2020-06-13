package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Circle
import model.Cut
import model.Mark
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
  }

  sealed class Output {
    data class Marks(val list: List<Mark>) : Output()
  }

  sealed class Input {
    data class AddNewMark(val circle: Circle, val cut: Cut) : Input()
  }
}
