package components.cutslider

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.observable.Observable
import components.cutslider.Slider.Dependencies
import model.Plane

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
    val sliderOutput: Consumer<Output>
    val sliderInput: Observable<Input>
    val plane: Plane
    val researchId: Int
  }

  sealed class Output {
    data class SliceNumberChanged(val sliceNumber: Int) : Output()
  }

  sealed class Input {
    data class ChangeSliceNumber(val sliceNumber: Int) : Input()
  }
}

@Suppress("FunctionName") // Factory function
fun Slider(
  componentContext: ComponentContext,
  dependencies: Dependencies
): Slider = SliderComponent(componentContext, dependencies)