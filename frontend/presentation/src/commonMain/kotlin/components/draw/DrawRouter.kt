package components.draw

import com.arkivanov.decompose.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.observable.Observable
import components.cutcontainer.CutContainer.DrawChild
import components.draw.Draw.Input
import components.draw.Draw.Output
import model.MyPlane

internal class DrawRouter(
  routerFactory: RouterFactory,
  private val drawFactory: (ComponentContext, Consumer<Output>, Observable<Input>) -> Draw,
  private val drawOutput: Consumer<Output>,
  private val drawInput: Observable<Input>,
  private val plane: MyPlane,
) {

  private val router =
    routerFactory.router<Config, DrawChild>(
      initialConfiguration = Config.Draw,
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