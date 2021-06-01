package components.brightness

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.brightness.Brightness.Dependencies
import repository.BrightnessRepository

interface Brightness {

  val model: Value<Model>

  fun onBlackChanged(value: Int)
  fun onWhiteChanged(value: Int)
  fun onGammaChanged(value: Double)

  data class Model(
    val blackValue: Int,
    val whiteValue: Int,
    val gammaValue: Double
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val brightnessRepository: BrightnessRepository
    val brightnessOutput: Consumer<Output>
    val researchId: Int
  }

  sealed class Output {
//    data class BlackChanged(val value: Int) : BrightnessView.Event()
//    data class WhiteChanged(val value: Int) : BrightnessView.Event()
//    data class GammaChanged(val value: Double) : BrightnessView.Event()
  }
}

@Suppress("FunctionName") // Factory function
fun Brightness(componentContext: ComponentContext, dependencies: Dependencies): Brightness =
  BrightnessComponent(componentContext, dependencies)