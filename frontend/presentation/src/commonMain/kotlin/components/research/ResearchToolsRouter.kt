package components.research

import com.arkivanov.decompose.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.badoo.reaktive.base.Consumer
import components.research.ResearchRoot.ToolsChild
import components.researchtools.ResearchTools
import model.ResearchDataModel

internal class ResearchToolsRouter(
  routerFactory: RouterFactory,
  private val toolsFactory: (ComponentContext, ResearchDataModel, Consumer<ResearchTools.Output>) -> ResearchTools,
  private val toolsOutput: Consumer<ResearchTools.Output>
) {

  private val router =
    routerFactory.router<Config, ToolsChild>(
      initialConfiguration = Config.None,
      key = "ResearchToolsRouter",
      childFactory = ::createChild
    )

  val state: Value<RouterState<Config, ToolsChild>> = router.state

  private fun createChild(config: Config, componentContext: ComponentContext): ToolsChild =
    when (config) {
      is Config.Tools -> ToolsChild.Data(toolsFactory(componentContext, config.data, toolsOutput))
      is Config.None -> ToolsChild.None
    }

  fun update(data: ResearchDataModel) {
    if (state.value.activeChild.instance is ToolsChild.None) {
      router.push(Config.Tools(data))
    }
  }

  sealed class Config : Parcelable {
    @Parcelize
    data class Tools(val data: ResearchDataModel) : Config()

    @Parcelize
    object None : Config()
  }
}