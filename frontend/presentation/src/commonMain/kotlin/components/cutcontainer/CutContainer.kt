package components.cutcontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cut.Cut
import components.cutcontainer.CutContainer.Dependencies
import components.cutslider.Slider
import components.draw.Draw
import components.shapes.Shapes
import model.CutType
import model.Plane
import model.ResearchDataModel
import repository.MyBrightnessRepository
import repository.MyMipRepository
import repository.MyResearchRepository

interface CutContainer {

  val sliderRouterState: Value<RouterState<*, SliderChild>>
  val drawRouterState: Value<RouterState<*, DrawChild>>
  val shapesRouterState: Value<RouterState<*, ShapesChild>>
  val cutRouterState: Value<RouterState<*, CutChild>>

  interface Dependencies {
    val storeFactory: StoreFactory
    val brightnessRepository: MyBrightnessRepository
    val researchRepository: MyResearchRepository
    val mipRepository: MyMipRepository
    val cutContainerOutput: Consumer<Output>
    val cutType: CutType
    val researchId: Int
    val data: ResearchDataModel
    val plane: Plane
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

  sealed class CutChild {
    data class Data(val component: Cut) : CutChild()
    object None : CutChild()
  }

  sealed class Output {
  }
}

@Suppress("FunctionName") // Factory function
fun CutContainer(componentContext: ComponentContext, dependencies: Dependencies): CutContainer =
  CutContainerComponent(
    componentContext = componentContext,
    dependencies = dependencies,
    slider = { childContext, output, input ->
      Slider(
        componentContext = childContext,
        dependencies = object : Slider.Dependencies, Dependencies by dependencies {
          override val sliderOutput: Consumer<Slider.Output> = output
          override val sliderInput: Consumer<Slider.Input> = input
        })
    },
    draw = { childContext, output, input ->
      Draw(
        componentContext = childContext,
        dependencies = object : Draw.Dependencies, Dependencies by dependencies {
          override val drawOutput: Consumer<Draw.Output> = output
          override val drawInput: Consumer<Draw.Input> = input
        })
    },
    shapes = { childContext, output, input ->
      Shapes(
        componentContext = childContext,
        dependencies = object : Shapes.Dependencies, Dependencies by dependencies {
          override val shapesOutput: Consumer<Shapes.Output> = output
          override val shapesInput: Consumer<Shapes.Input> = input
        })
    },
    cut = { childContext, output, input ->
      Cut(
        componentContext = childContext,
        dependencies = object : Cut.Dependencies, Dependencies by dependencies {
          override val cutOutput: Consumer<Cut.Output> = output
          override val cutInput: Consumer<Cut.Input> = input
        })
    },
  )