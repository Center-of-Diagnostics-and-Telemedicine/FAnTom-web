package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Cut
import model.CutType
import repository.ResearchRepository
import view.ShapesView

interface ShapesController {

  val input: (Input) -> Unit

  fun onViewCreated(
    shapesView: ShapesView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val researchRepository: ResearchRepository
    val lifecycle: Lifecycle
    val cut: Cut
    val researchId: Int
    val shapesOutput: (Output) -> Unit
  }

  sealed class Input {
    data class HorizontalLineChange(val line: Int) : Input()
    data class VerticalLineChange(val line: Int) : Input()
    data class SliceNumberChanged(val sliceNumber: Int) : Input()
  }

  sealed class Output {
    data class ChangeCutType(val type: CutType) : Output()
  }
}
