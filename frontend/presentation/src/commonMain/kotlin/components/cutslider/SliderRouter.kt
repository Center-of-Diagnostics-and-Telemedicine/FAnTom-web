package components.cutslider

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.cutcontainer.CutContainer.SliderChild
import components.cutslider.Slider.Input
import components.cutslider.Slider.Output
import model.CutType

internal class SliderRouter(
  routerFactory: RouterFactory,
  private val sliderFactory: (ComponentContext, Consumer<Output>, Consumer<Input>) -> Slider,
  private val sliderOutput: Consumer<Output>,
  private val sliderInput: Consumer<Input>,
  cutType: CutType
) {

  private val router =
    routerFactory.router(
      initialConfiguration = if (cutType == CutType.EMPTY) Config.None else Config.Slider,
      key = "SliderRouter",
      childFactory = ::createChild
    )

  val state: Value<RouterState<Config, SliderChild>> = router.state

  private fun createChild(config: Config, componentContext: ComponentContext): SliderChild =
    when (config) {
      is Config.Slider ->
        SliderChild.Data(sliderFactory(componentContext, sliderOutput, sliderInput))
      is Config.None -> SliderChild.None
    }

  fun show() {
    if (state.value.activeChild.instance is SliderChild.None) {
      router.replaceCurrent(Config.Slider)
    }
  }

  fun hide() {
    if (state.value.activeChild.instance is SliderChild.Data) {
      router.replaceCurrent(Config.None)
    }
  }

  sealed class Config : Parcelable {
    @Parcelize
    object Slider : Config()

    @Parcelize
    object None : Config()
  }
}