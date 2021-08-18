package components.cut

import com.arkivanov.decompose.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.observable.Observable
import components.cut.Cut.Input
import components.cut.Cut.Output
import components.cutcontainer.CutContainer.CutChild
import model.MyPlane

internal class CutRouter(
  routerFactory: RouterFactory,
  private val cutFactory: (ComponentContext, Consumer<Output>, Observable<Input>) -> Cut,
  private val cutOutput: Consumer<Output>,
  private val cutInput: Observable<Input>,
  private val plane: MyPlane
) {

  private val router =
    routerFactory.router<Config, CutChild>(
      initialConfiguration = Config.Cut,
      key = "CutRouter",
      childFactory = ::createChild
    )

  val state: Value<RouterState<Config, CutChild>> = router.state

  private fun createChild(config: Config, componentContext: ComponentContext): CutChild =
    when (config) {
      is Config.Cut -> CutChild.Data(cutFactory(componentContext, cutOutput, cutInput))
      is Config.None -> CutChild.None
    }

  fun show() {
    if (state.value.activeChild.instance is CutChild.None) {
      router.replaceCurrent(Config.Cut)
    }
  }

  fun hide() {
    if (state.value.activeChild.instance is CutChild.Data) {
      router.replaceCurrent(Config.None)
    }
  }

  sealed class Config : Parcelable {
    @Parcelize
    object Cut : Config()

    @Parcelize
    object None : Config()
  }
}