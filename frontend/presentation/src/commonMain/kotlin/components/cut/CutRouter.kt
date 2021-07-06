package components.cut

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterFactory
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.replaceCurrent
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.cut.Cut.Output
import components.cutcontainer.CutContainer.CutChild
import model.CutType

internal class CutRouter(
  routerFactory: RouterFactory,
  private val cutFactory: (ComponentContext, Consumer<Cut.Input>, Consumer<Output>) -> Cut,
  private val cutOutput: Consumer<Output>,
  private val cutType: CutType,
  private val cutInput: Consumer<Cut.Input>
) {

  private val router =
    routerFactory.router(
      initialConfiguration = if (cutType == CutType.EMPTY) Config.None else Config.Cut,
      key = "CutRouter",
      childFactory = ::createChild
    )

  val state: Value<RouterState<Config, CutChild>> = router.state

  private fun createChild(config: Config, componentContext: ComponentContext): CutChild =
    when (config) {
      is Config.Cut -> CutChild.Data(cutFactory(componentContext, cutInput, cutOutput))
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