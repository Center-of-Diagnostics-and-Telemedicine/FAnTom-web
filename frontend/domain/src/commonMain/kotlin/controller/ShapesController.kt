package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Circle
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
    data class SliceNumberChanged(val sliceNumber: Int) : Input()
    class ExternalSliceNumberChanged(val sliceNumber: Int, val cut: Cut) : Input()
    class MousePosition(val dicomX: Double, val dicomY: Double, val cutType: CutType) :
      Input()

    class Drawing(val circle: Circle, val cutType: CutType) : Input()
  }

  sealed class Output {
//    data class ChangeCutType(val type: CutType) : Output()
  }
}
