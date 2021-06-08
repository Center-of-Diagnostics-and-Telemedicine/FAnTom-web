package components.cut

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cutslider.Slider
import components.cut.Cut.Dependencies
import model.Mip

interface Cut {

  val model: Value<Model>

  val slider: Slider

  data class Model(
    val sliceNumber: Int,
    val slice: String,
    val black: Int,
    val white: Int,
    val gamma: Double,
    val mipMethod: Mip,
    val mipValue: Int,
    val mainLoading: Boolean,
    val secondaryLoading: Boolean,
    val error: String
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val cutOutput: Consumer<Output>
  }

  sealed class Output {
  }
}

@Suppress("FunctionName") // Factory function
fun Cut(componentContext: ComponentContext, dependencies: Dependencies): Cut =
  CutComponent(componentContext, dependencies)