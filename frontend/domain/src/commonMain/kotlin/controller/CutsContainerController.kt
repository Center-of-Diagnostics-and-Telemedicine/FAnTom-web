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
    val data: ResearchData
  }

  sealed class Input {
    data class ChangeGrid(val grid: GridModel) : Input()
    data class ChangeCutType(val cutType: CutType, val cut: Plane) : Input()

    object Idle : Input()
  }

  sealed class Output {
    data class OpenFullCut(val cut: Plane) : Output()
    data class CloseFullCut(val cut: Plane) : Output()
    data class CircleDrawn(val circle: Circle, val sliceNumber: Int, val cut: Plane) : Output()
    data class RectangleDrawn(val rectangle: Rectangle, val sliceNumber: Int, val cut: Plane) : Output()
    data class SelectMark(val mark: MarkModel) : Output()
    data class UnselectMark(val mark: MarkModel) : Output()
    data class ContrastBrightnessChanged(val black: Int, val white: Int) : Output()
    data class UpdateMarkWithoutSave(val markToUpdate: MarkModel) : Output()
    data class UpdateMarkWithSave(val mark: MarkModel) : Output()
    data class ChangeCutType(val cutType: CutType, val cut: Plane) : CutsContainerController.Output()
  }
}
