package components.draw

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.cutcontainer.CutContainer.DrawChild
import components.draw.Draw.Output
import components.draw.Draw.Input
import model.CutType

internal class DrawRouter(
  routerFactory: RouterFactory,
  private val drawFactory: (ComponentContext, Consumer<Output>, Consumer<Input>) -> Draw,
  private val drawOutput: Consumer<Output>,
  private val drawInput: Consumer<Input>,
  private val cutType: CutType,
) {

  private val router =
    routerFactory.router(
      initialConfiguration = if (cutType == CutType.EMPTY) Config.None else Config.Draw,
      key = "DrawRouter",
      childFactory = ::createChild
    )

  val state: Value<RouterState<Config, DrawChild>> = router.state

  private fun createChild(config: Config, componentContext: ComponentContext): DrawChild =
    when (config) {
      is Config.Draw -> DrawChild.Data(drawFactory(componentContext, drawOutput, drawInput))
      is Config.None -> DrawChild.None
    }

  fun show() {
    if (state.value.activeChild.instance is DrawChild.None) {
      router.push(Config.Draw)
    }
  }

  fun hide() {
    if (state.value.activeChild.instance is DrawChild.Data) {
      router.replaceCurrent(Config.None)
    }
  }

  sealed class Config : Parcelable {
    @Parcelize
    object Draw : Config()

    @Parcelize
    object None : Config()
  }
}