package components.shapes

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.cut.Cut.ShapesChild
import components.shapes.Shapes.Output

internal class ShapesRouter(
  routerFactory: RouterFactory,
  private val shapesFactory: (ComponentContext, Consumer<Output>) -> Shapes,
  private val shapesOutput: Consumer<Output>
) {

  private val router =
    routerFactory.router<Config, ShapesChild>(
      initialConfiguration = Config.None,
      key = "ShapesRouter",
      childFactory = ::createChild
    )

  val state: Value<RouterState<Config, ShapesChild>> = router.state

  private fun createChild(config: Config, componentContext: ComponentContext): ShapesChild =
    when (config) {
      is Config.Shapes -> ShapesChild.Data(shapesFactory(componentContext, shapesOutput))
      is Config.None -> ShapesChild.None
    }

  fun update() {
    if (state.value.activeChild.instance is ShapesChild.None) {
      router.push(Config.Shapes)
    }
  }

  sealed class Config : Parcelable {
    @Parcelize
    object Shapes : Config()

    @Parcelize
    object None : Config()
  }
}