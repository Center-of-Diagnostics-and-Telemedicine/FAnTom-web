package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Circle
import model.Cut
import model.Grid
import model.ResearchSlicesSizesData
import view.CutsContainerView

interface CutsContainerController {

  val input: (Input) -> Unit

  fun onViewCreated(
    cutsContainerView: CutsContainerView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val data: ResearchSlicesSizesData
  }

  sealed class Input {
    data class GridChanged(val grid: Grid) : Input()
  }

  sealed class Output {
    data class OpenFullCut(val cut: Cut) : Output()
    data class CloseFullCut(val cut: Cut) : Output()
    data class CircleDrawn(val circle: Circle, val sliceNumber: Int, val cut: Cut) : Output()
  }
}
