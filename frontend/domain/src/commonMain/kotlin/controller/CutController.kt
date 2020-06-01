package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Cut
import model.Mip
import model.Presets
import repository.ResearchRepository
import view.CutView

interface CutController {

  val input: (Input) -> Unit

  fun onViewCreated(
    cutView: CutView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val researchRepository: ResearchRepository
    val cutOutput: (Output) -> Unit
    val cut: Cut
    val researchId: Int
  }

  sealed class Input {
    data class BlackChanged(val value: Int) : Input()
    data class WhiteChanged(val value: Int) : Input()
    data class GammaChanged(val value: Double) : Input()
    data class MipMethodChanged(val value: Mip) : Input()
    data class MipValueChanged(val value: Int) : Input()
    data class PresetChanged(val preset: Presets) : Input()
    data class SliceNumberChanged(val sliceNumber: Int) : Input()
  }

  sealed class Output {
    data class SliceNumberChanged(val sliceNumber: Int, val cut: Cut): Output()
  }
}
