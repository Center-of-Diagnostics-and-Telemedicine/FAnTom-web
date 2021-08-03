package components.cutcontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.observe
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.subject.publish.PublishSubject
import components.Consumer
import components.asValue
import components.cut.Cut
import components.cut.CutRouter
import components.cutcontainer.CutContainer.*
import components.cutslider.Slider
import components.cutslider.SliderRouter
import components.draw.Draw
import components.draw.DrawRouter
import components.getStore
import components.shapes.Shapes
import components.shapes.ShapesRouter
import model.toShape
import store.cut.CutContainerStore

class CutContainerComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  cut: (ComponentContext, Consumer<Cut.Output>, Observable<Cut.Input>) -> Cut,
  slider: (ComponentContext, Consumer<Slider.Output>, Observable<Slider.Input>) -> Slider,
  draw: (ComponentContext, Consumer<Draw.Output>, Observable<Draw.Input>) -> Draw,
  shapes: (ComponentContext, Consumer<Shapes.Output>, Observable<Shapes.Input>) -> Shapes,
) : CutContainer, ComponentContext by componentContext, Dependencies by dependencies {

  private val cutInput: Subject<Cut.Input> = PublishSubject()
  private val shapesInput: Subject<Shapes.Input> = PublishSubject()
  private val drawInput: Subject<Draw.Input> = PublishSubject()
  private val sliderInput: Subject<Slider.Input> = PublishSubject()

  val store = instanceKeeper.getStore {
    CutContainerStoreProvider(
      storeFactory = storeFactory,
      brightnessRepository = brightnessRepository,
      mipRepository = mipRepository,
      researchId = researchId,
      researchRepository = researchRepository,
      marksRepository = marksRepository,
      plane = plane,
      data = data
    ).provide()
  }

  private val sliderRouter =
    SliderRouter(
      routerFactory = this,
      sliderFactory = slider,
      sliderOutput = Consumer(::onSliderOutput),
      sliderInput = sliderInput,
      plane = plane
    )

  override val sliderRouterState: Value<RouterState<*, SliderChild>> = sliderRouter.state

  private val drawRouter =
    DrawRouter(
      routerFactory = this,
      drawFactory = draw,
      drawOutput = Consumer(::onDrawOutput),
      drawInput = drawInput,
      cutType = cutType
    )
  override val drawRouterState: Value<RouterState<*, DrawChild>> = drawRouter.state

  private val shapesRouter =
    ShapesRouter(
      routerFactory = this,
      shapesFactory = shapes,
      shapesOutput = Consumer(::onShapesOutput),
      shapesInput = shapesInput,
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

  init {
    store.asValue().observe(lifecycle) { state ->

      state.marks
        .mapNotNull { markModel ->
          markModel.toShape(plane, state.cutModel.sliceNumber)
        }
        .let { shapes ->
          shapesInput.onNext(Shapes.Input.Shapes(shapes))
        }

      state.cutModel
        .let { cutModel ->
          cutInput.onNext(Cut.Input.ChangeCutModel(cutModel))
        }

    }
  }

  private fun onSliderOutput(output: Slider.Output) {
    when (output) {
      is Slider.Output.SliceNumberChanged ->
        store.accept(CutContainerStore.Intent.ChangeSliceNumber(output.sliceNumber))
    }
  }

  private fun onDrawOutput(output: Draw.Output) {
    when (output) {
      is Draw.Output.Circle -> store.accept(CutContainerStore.Intent.HandleNewShape(output.circle))
      is Draw.Output.Ellipse -> store.accept(CutContainerStore.Intent.HandleNewShape(output.ellipse))
      is Draw.Output.Rectangle -> store.accept(CutContainerStore.Intent.HandleNewShape(output.rectangle))
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