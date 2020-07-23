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
    val data: ResearchSlicesSizesDataNew
  }

  sealed class Input {
    data class GridChanged(val grid: Grid) : Input()
    data class ChangeCutType(val cutType: CutType, val cut: Cut) : Input()

    object Idle : Input()
  }

  sealed class Output {
    data class OpenFullCut(val cut: Cut) : Output()
    data class CloseFullCut(val cut: Cut) : Output()
    data class CircleDrawn(val circle: Circle, val sliceNumber: Int, val cut: Cut) : Output()
    data class SelectMark(val mark: MarkModel) : Output()
    data class UnselectMark(val mark: MarkModel) : Output()
    data class ContrastBrightnessChanged(val black: Int, val white: Int) : Output()
    data class UpdateMark(val markToUpdate: MarkModel) : Output()
    data class UpdateMarkWithSave(val mark: MarkModel) : Output()
    data class ChangeCutType(val cutType: CutType, val cut: Cut) : CutsContainerController.Output()
  }
}
