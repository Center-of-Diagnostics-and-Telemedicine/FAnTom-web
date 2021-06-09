package components.cutslider

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import controller.SliderController
import components.cutslider.Slider.Dependencies

interface Slider {

  val model: Value<Model>

  fun onValueChange(value: Int)

  data class Model(
    val currentValue: Int,
    val maxValue: Int,
    val defaultValue: Int
  )

  interface Dependencies {
    val storeFactory: StoreFactory
//    val cutRepository: CutRepository
    val sliderOutput: Consumer<Output>
    val researchId: Int
  }

  sealed class Output {
    data class SliceNumberChanged(val sliceNumber: Int)
  }
}

@Suppress("FunctionName") // Factory function
fun Slider(componentContext: ComponentContext, dependencies: Dependencies): Slider =
  SliderComponent(componentContext, dependencies)