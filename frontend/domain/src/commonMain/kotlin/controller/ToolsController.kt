package controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.*
import repository.BrightnessRepository
import repository.MipRepository
import view.*

interface ToolsController {

  val input: (Input) -> Unit

  fun onViewCreated(
    toolsView: ToolsView,
    gridView: GridView,
    mipView: MipView,
    brightnessView: BrightnessView,
    presetView: PresetView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val toolsOutput: (Output) -> Unit
    val data: ResearchData
    val mipRepository: MipRepository
    val brightnessRepository: BrightnessRepository
  }

  sealed class Output {
    data class GridChanged(val grid: GridModel) : Output()
    data class BlackChanged(val value: Int) : Output()
    data class WhiteChanged(val value: Int) : Output()
    data class GammaChanged(val value: Double) : Output()
    data class MipMethodChanged(val mip: Mip) : Output()
    data class MipValueChanged(val value: Int) : Output()
    data class PresetChanged(val preset: Presets) : Output()
    object Back : Output()
    object Close : Output()
  }

  sealed class Input {
    data class ContrastBrightnessChanged(val black: Int, val white: Int) : Input()
    data class OpenFullCut(val cut: Plane) : Input()
    data class ReturnPreviousGrid(val cut: Plane) : Input()
    object Idle : Input()
  }
}
