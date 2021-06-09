package components.cut

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.asValue
import components.cut.Cut.*
import components.cutslider.Slider
import components.cutslider.SliderRouter
import components.draw.Draw
import components.draw.DrawRouter
import components.getStore
import components.shapes.Shapes
import components.shapes.ShapesRouter

class CutComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  slider: (ComponentContext, Consumer<Slider.Output>) -> Slider,
  draw: (ComponentContext, Consumer<Draw.Output>) -> Draw,
  shapes: (ComponentContext, Consumer<Shapes.Output>) -> Shapes,
) : Cut, ComponentContext by componentContext, Dependencies by dependencies {

  val store = instanceKeeper.getStore {
    MyCutStoreProvider(
      storeFactory = storeFactory,
      brightnessRepository = brightnessRepository,
      mipRepository = mipRepository,
      researchId = researchId
    ).provide()
  }

  override val model: Value<Model> = store.asValue().map(stateToModel)

  private val sliderRouter =
    SliderRouter(
      routerFactory = this,
      sliderFactory = slider,
      sliderOutput = Consumer(::onSliderOutput)
    )

  override val sliderRouterState: Value<RouterState<*, SliderChild>> = sliderRouter.state

  private val drawRouter =
    DrawRouter(
      routerFactory = this,
      drawFactory = draw,
      drawOutput = Consumer(::onDrawOutput)
    )
  override val drawRouterState: Value<RouterState<*, DrawChild>> = drawRouter.state


  private val shapesRouter =
    ShapesRouter(
      routerFactory = this,
      shapesFactory = shapes,
      shapesOutput = Consumer(::onShapesOutput)
    )

  override val shapesRouterState: Value<RouterState<*, ShapesChild>> = shapesRouter.state

  private fun onSliderOutput(output: Slider.Output) {
    when (output) {
      else -> throw NotImplementedError("onSliderOutput in CutComponent not implemented")
    }
  }

  private fun onDrawOutput(output: Draw.Output) {
    when (output) {
      else -> throw NotImplementedError("onDrawOutput in CutComponent not implemented")
    }
  }

  private fun onShapesOutput(output: Shapes.Output) {
    when (output) {
      else -> throw NotImplementedError("onShapesOutput in CutComponent not implemented")
    }
  }
}