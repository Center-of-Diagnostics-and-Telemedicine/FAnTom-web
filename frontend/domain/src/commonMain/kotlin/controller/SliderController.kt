package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import model.Cut
import view.SliderView

interface SliderController {

  fun onViewCreated(
    sliderView: SliderView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val cut: Cut
    val researchId: Int
    val sliderOutput: (Output) -> Unit
  }

  sealed class Output {
    data class SliceNumberChanged(val sliceNumber: Int) : Output()
  }
}
