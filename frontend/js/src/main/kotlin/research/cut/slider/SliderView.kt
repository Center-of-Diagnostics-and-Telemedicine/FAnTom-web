package research.cut.slider

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.doOnBeforeNext
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.publish.PublishSubject
import com.ccfraser.muirwik.components.MSliderOrientation
import com.ccfraser.muirwik.components.MSliderValueLabelDisplay
import com.ccfraser.muirwik.components.mSlider
import controller.SliderController
import controller.SliderControllerImpl
import destroy
import model.Plane
import model.Research
import react.*
import resume
import view.SliderView
import view.initialSliderModel

class SliderComponent(prps: SliderProps) : RComponent<SliderProps, SliderState>(prps) {

  private val sliderViewDelegate = SliderViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: SliderController
  private val sliderInput = PublishSubject<SliderController.Input>()

  init {
    state = SliderState(initialSliderModel())
  }

  override fun componentDidMount() {
    lifecycleRegistry.resume()
    controller = createController()
    controller.onViewCreated(sliderViewDelegate, lifecycleRegistry)
  }

  private fun createController(): SliderController {
    val dependencies = props.dependencies
    val disposable = dependencies.sliderInput.doOnBeforeNext { console.log("sliderInput YO") }.subscribe { controller.input(it) }
    val sliderControllerDependencies =
      object : SliderController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
      }
    lifecycleRegistry.doOnDestroy(disposable::dispose)
    return SliderControllerImpl(sliderControllerDependencies)
  }

  override fun RBuilder.render() {
    val model = state.sliderModel
    mSlider(
      value = model.currentValue,
      defaultValue = model.defaultValue,
      min = 1,
      max = model.maxValue,
      orientation = MSliderOrientation.horizontal,
      valueLabelDisplay = MSliderValueLabelDisplay.auto,
      onChange = { _, newValue ->
        sliderViewDelegate.dispatch(SliderView.Event.HandleOnChange(newValue.toInt()))
      }
    )
  }

  private fun updateState(model: SliderView.Model) {
    setState { sliderModel = model }
  }

  override fun componentWillUnmount() {
    lifecycleRegistry.destroy()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val cut: Plane
    val sliderOutput: (SliderController.Output) -> Unit
    val sliderInput: Observable<SliderController.Input>
    val research: Research
  }

}

class SliderState(
  var sliderModel: SliderView.Model,
) : RState

interface SliderProps : RProps {
  var dependencies: SliderComponent.Dependencies
}

fun RBuilder.sliderView(
  dependencies: SliderComponent.Dependencies,
) = child(SliderComponent::class) {
  attrs.dependencies = dependencies
}
