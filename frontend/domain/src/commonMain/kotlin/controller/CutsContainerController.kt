package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.*
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
    data class SelectMark(val mark: MarkDomain) : Output()
    data class UnselectMark(val mark: MarkDomain) : Output()
    data class ContrastBrightnessChanged(val black: Int, val white: Int) : Output()
    data class UpdateMark(val markToUpdate: MarkDomain) : Output()
  }
}
