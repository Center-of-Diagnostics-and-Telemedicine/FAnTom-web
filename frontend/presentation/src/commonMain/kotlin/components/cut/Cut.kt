package components.cut

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cut.Cut.Dependencies
import components.cutslider.Slider
import components.draw.Draw
import components.shapes.Shapes
import model.CutType
import model.Mip
import repository.MyBrightnessRepository
import repository.MyMipRepository

interface Cut {

  val model: Value<Model>

  val sliderRouterState: Value<RouterState<*, SliderChild>>
  val drawRouterState: Value<RouterState<*, DrawChild>>
  val shapesRouterState: Value<RouterState<*, ShapesChild>>

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
    val brightnessRepository: MyBrightnessRepository
    val mipRepository: MyMipRepository
    val cutOutput: Consumer<Output>
    val cutType: CutType
    val researchId: Int
  }

  sealed class SliderChild {
    data class Data(val component: Slider) : SliderChild()
    object None : SliderChild()
  }

  sealed class DrawChild {
    data class Data(val component: Draw) : DrawChild()
    object None : DrawChild()
  }

  sealed class ShapesChild {
    data class Data(val component: Shapes) : ShapesChild()
    object None : ShapesChild()
  }

  sealed class Output {
  }
}

@Suppress("FunctionName") // Factory function
fun Cut(componentContext: ComponentContext, dependencies: Dependencies): Cut =
  CutComponent(
    componentContext = componentContext,
    dependencies = dependencies,
    slider = { childContext, output ->
      Slider(
        componentContext = childContext,
        dependencies = object : Slider.Dependencies, Dependencies by dependencies {
          override val sliderOutput: Consumer<Slider.Output> = output
        })
    },
    draw = { childContext, output ->
      Draw(
        componentContext = childContext,
        dependencies = object : Draw.Dependencies, Dependencies by dependencies {
          override val drawOutput: Consumer<Draw.Output> = output
        })
    },
    shapes = { childContext, output ->
      Shapes(
        componentContext = childContext,
        dependencies = object : Shapes.Dependencies, Dependencies by dependencies {
          override val shapesOutput: Consumer<Shapes.Output> = output
        })
    },
  )