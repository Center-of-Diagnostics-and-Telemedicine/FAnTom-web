package research.cut.slider

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.ccfraser.muirwik.components.MSliderOrientation
import com.ccfraser.muirwik.components.MSliderValueLabelDisplay
import com.ccfraser.muirwik.components.mSlider
import controller.SliderController
import controller.SliderControllerImpl
import model.Cut
import react.*
import resume
import view.SliderView
import view.initialSliderModel

class SliderComponent(prps: SliderProps) : RComponent<SliderProps, SliderState>(prps) {

  private val sliderViewDelegate = SliderViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: SliderController

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
    val cutControllerDependencies =
      object : SliderController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
      }
    return SliderControllerImpl(cutControllerDependencies)
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

  interface Dependencies {
    val storeFactory: StoreFactory
    val cut: Cut
    val sliderOutput: (SliderController.Output) -> Unit
    val researchId: Int
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
