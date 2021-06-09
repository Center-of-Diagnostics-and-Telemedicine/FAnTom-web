package components.cutslider

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.cut.Cut.SliderChild
import components.cutslider.Slider.Output

internal class SliderRouter(
  routerFactory: RouterFactory,
  private val sliderFactory: (ComponentContext, Consumer<Output>) -> Slider,
  private val sliderOutput: Consumer<Output>
) {

  private val router =
    routerFactory.router<Config, SliderChild>(
      initialConfiguration = Config.None,
      key = "SliderRouter",
      childFactory = ::createChild
    )

  val state: Value<RouterState<Config, SliderChild>> = router.state

  private fun createChild(config: Config, componentContext: ComponentContext): SliderChild =
    when (config) {
      is Config.Slider -> SliderChild.Data(sliderFactory(componentContext, sliderOutput))
      is Config.None -> SliderChild.None
    }

  fun update() {
    if (state.value.activeChild.instance is SliderChild.None) {
      router.push(Config.Slider)
    }
  }

  sealed class Config : Parcelable {
    @Parcelize
    object Slider : Config()

    @Parcelize
    object None : Config()
  }
}