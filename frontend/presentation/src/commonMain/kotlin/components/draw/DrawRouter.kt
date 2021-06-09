package components.draw

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.cut.Cut.DrawChild
import components.draw.Draw.Output

internal class DrawRouter(
  routerFactory: RouterFactory,
  private val drawFactory: (ComponentContext, Consumer<Output>) -> Draw,
  private val drawOutput: Consumer<Output>
) {

  private val router =
    routerFactory.router<Config, DrawChild>(
      initialConfiguration = Config.None,
      key = "DrawRouter",
      childFactory = ::createChild
    )

  val state: Value<RouterState<Config, DrawChild>> = router.state

  private fun createChild(config: Config, componentContext: ComponentContext): DrawChild =
    when (config) {
      is Config.Draw -> DrawChild.Data(drawFactory(componentContext, drawOutput))
      is Config.None -> DrawChild.None
    }

  fun update() {
    if (state.value.activeChild.instance is DrawChild.None) {
      router.push(Config.Draw)
    }
  }

  sealed class Config : Parcelable {
    @Parcelize
    object Draw : Config()

    @Parcelize
    object None : Config()
  }
}