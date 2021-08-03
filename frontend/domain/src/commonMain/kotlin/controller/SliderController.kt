package controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Plane
import model.Research
import view.SliderView

interface SliderController {

  val input: (Input) -> Unit

  fun onViewCreated(
    sliderView: SliderView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val cut: Plane
    val research: Research
    val sliderOutput: (Output) -> Unit
  }

  sealed class Input{
    data class SliceNumberChanged(val sliceNumber: Int) : Input()
  }

  sealed class Output {
    data class SliceNumberChanged(val sliceNumber: Int) : Output()
  }
}
