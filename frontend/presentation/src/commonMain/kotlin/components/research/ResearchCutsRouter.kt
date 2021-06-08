package components.research

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.cutscontainer.CutsContainer
import components.research.ResearchRoot.CutsChild
import model.ResearchData

internal class ResearchCutsRouter(
  routerFactory: RouterFactory,
  private val cutsFactory: (ComponentContext, ResearchData, Consumer<CutsContainer.Output>) -> CutsContainer,
  private val cutsOutput: Consumer<CutsContainer.Output>
) {

  private val router =
    routerFactory.router<Config, CutsChild>(
      initialConfiguration = Config.None,
      key = "ResearchCutsRouter",
      childFactory = ::createChild
    )

  val state: Value<RouterState<Config, CutsChild>> = router.state

  private fun createChild(
    config: Config,
    componentContext: ComponentContext
  ): CutsChild =
    when (config) {
      is Config.Marks -> CutsChild.Data(cutsFactory(componentContext, config.data, cutsOutput))
      is Config.None -> CutsChild.None
    }

  fun update(data: ResearchData) {
    if (state.value.activeChild.instance is CutsChild.None) {
      router.push(Config.Marks(data))
    }
  }

  sealed class Config : Parcelable {
    //    @Parcelize
    data class Marks(val data: ResearchData) : Config()

    //    @Parcelize
    object None : Config()
  }
}