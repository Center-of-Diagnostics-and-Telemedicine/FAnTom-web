package components.shapes

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.cutcontainer.CutContainer.ShapesChild
import components.shapes.Shapes.Output
import components.shapes.Shapes.Input
import model.CutType

internal class ShapesRouter(
  routerFactory: RouterFactory,
  private val shapesFactory: (ComponentContext, Consumer<Output>, Consumer<Input>) -> Shapes,
  private val shapesOutput: Consumer<Output>,
  private val shapesInput: Consumer<Input>,
  cutType: CutType
) {

  private val router =
    routerFactory.router(
      initialConfiguration = if (cutType == CutType.EMPTY) Config.None else Config.Shapes,
      key = "ShapesRouter",
      childFactory = ::createChild
    )

  val state: Value<RouterState<Config, ShapesChild>> = router.state

  private fun createChild(config: Config, componentContext: ComponentContext): ShapesChild =
    when (config) {
      is Config.Shapes ->
        ShapesChild.Data(shapesFactory(componentContext, shapesOutput, shapesInput))
      is Config.None -> ShapesChild.None
    }

  fun show() {
    if (state.value.activeChild.instance is ShapesChild.None) {
      router.push(Config.Shapes)
    }
  }

  fun hide() {
    if (state.value.activeChild.instance is ShapesChild.Data) {
      router.replaceCurrent(Config.None)
    }
  }

  sealed class Config : Parcelable {
    @Parcelize
    object Shapes : Config()

    @Parcelize
    object None : Config()
  }
}