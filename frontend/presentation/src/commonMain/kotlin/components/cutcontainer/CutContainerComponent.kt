package components.cutcontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.subject.publish.PublishSubject
import components.Consumer
import components.cut.Cut
import components.cut.CutRouter
import components.cutcontainer.CutContainer.CutChild
import components.cutcontainer.CutContainer.Dependencies
import components.cutcontainer.CutContainer.DrawChild
import components.cutcontainer.CutContainer.ShapesChild
import components.cutcontainer.CutContainer.SliderChild
import components.cutslider.Slider
import components.cutslider.SliderRouter
import components.draw.Draw
import components.draw.DrawRouter
import components.getStore
import components.shapes.Shapes
import components.shapes.ShapesRouter

class CutContainerComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  cut: (ComponentContext, Consumer<Cut.Input>, Consumer<Cut.Output>) -> Cut,
  slider: (ComponentContext, Consumer<Slider.Output>) -> Slider,
  draw: (ComponentContext, Consumer<Draw.Output>) -> Draw,
  shapes: (ComponentContext, Consumer<Shapes.Output>) -> Shapes,
) : CutContainer, ComponentContext by componentContext, Dependencies by dependencies {

  private val cutInput: Subject<Cut.Input> = PublishSubject()

  val store = instanceKeeper.getStore {
    CutContainerStoreProvider(
      storeFactory = storeFactory,
      brightnessRepository = brightnessRepository,
      mipRepository = mipRepository,
      researchId = researchId,
      researchRepository = researchRepository,
      plane = plane
    ).provide()
  }

  private val sliderRouter =
    SliderRouter(
      routerFactory = this,
      sliderFactory = slider,
      sliderOutput = Consumer(::onSliderOutput),
      cutType = cutType
    )

  override val sliderRouterState: Value<RouterState<*, SliderChild>> = sliderRouter.state

  private val drawRouter =
    DrawRouter(
      routerFactory = this,
      drawFactory = draw,
      drawOutput = Consumer(::onDrawOutput),
      cutType = cutType
    )
  override val drawRouterState: Value<RouterState<*, DrawChild>> = drawRouter.state


  private val shapesRouter =
    ShapesRouter(
      routerFactory = this,
      shapesFactory = shapes,
      shapesOutput = Consumer(::onShapesOutput),
      cutType = cutType
    )

  override val shapesRouterState: Value<RouterState<*, ShapesChild>> = shapesRouter.state

  private val cutRouter =
    CutRouter(
      routerFactory = this,
      cutFactory = cut,
      cutOutput = Consumer(::onCutOutput),
      cutInput = cutInput,
      cutType = cutType
    )

  override val cutRouterState: Value<RouterState<*, CutChild>> = cutRouter.state

  private fun onSliderOutput(output: Slider.Output) {
    when (output) {
      is Slider.Output.SliceNumberChanged -> cutInput.onNext(Cut.Input.SliceNumberChanged(output.sliceNumber))
      else -> throw NotImplementedError("onSliderOutput in CutComponent not implemented for $output")
    }
  }

  private fun onDrawOutput(output: Draw.Output) {
    when (output) {
      else -> throw NotImplementedError("onDrawOutput in CutComponent not implemented for $output")
    }
  }

  private fun onShapesOutput(output: Shapes.Output) {
    when (output) {
      else -> throw NotImplementedError("onShapesOutput in CutComponent not implemented for $output")
    }
  }

  private fun onCutOutput(output: Cut.Output) {
    when (output) {
      else -> throw NotImplementedError("onCutOutput in CutComponent not implemented for $output")
    }
  }
}