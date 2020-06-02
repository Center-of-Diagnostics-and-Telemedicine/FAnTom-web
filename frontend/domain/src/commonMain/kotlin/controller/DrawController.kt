package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Circle
import model.Cut
import model.CutType
import view.DrawView

interface DrawController {

  fun onViewCreated(
    drawView: DrawView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val cut: Cut
    val researchId: Int
    val drawOutput: (Output) -> Unit
  }

  sealed class Output {
    data class OnClick(
      val dicomX: Double,
      val dicomY: Double,
      val altKeyPressed: Boolean,
      val cutType: CutType
    ) : Output()

    data class ChangeContrastBrightness(val deltaX: Double, val deltaY: Double) : Output()
    data class Drawing(val circle: Circle, val cutType: CutType) : Output()
    class StartMoving(val startDicomX: Double, val startDicomY: Double, val cutType: CutType) :
      Output()

    data class MousePosition(val dicomX: Double, val dicomY: Double, val cutType: CutType) : Output()
    data class Drawn(val circle: Circle, val cutType: CutType) : Output()
    data class ChangeSlice(val deltaY: Double, val type: CutType) : Output()
  }
}
