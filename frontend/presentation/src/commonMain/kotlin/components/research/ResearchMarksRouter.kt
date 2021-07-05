package components.research

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.research.ResearchRoot.MarksChild
import components.researchmarks.ResearchMarks
import model.ResearchData
import model.ResearchDataModel

internal class ResearchMarksRouter(
  routerFactory: RouterFactory,
  private val marksFactory: (ComponentContext, ResearchDataModel, Consumer<ResearchMarks.Output>) -> ResearchMarks,
  private val marksOutput: Consumer<ResearchMarks.Output>
) {

  private val router =
    routerFactory.router<Config, MarksChild>(
      initialConfiguration = Config.None,
      key = "ResearchMarksRouter",
      childFactory = ::createChild
    )

  val state: Value<RouterState<Config, MarksChild>> = router.state

  private fun createChild(config: Config, componentContext: ComponentContext): MarksChild =
    when (config) {
      is Config.Marks -> MarksChild.Data(marksFactory(componentContext, config.data, marksOutput))
      is Config.None -> MarksChild.None
    }

  fun update(data: ResearchDataModel) {
    if (state.value.activeChild.instance is MarksChild.None) {
      router.push(Config.Marks(data))
    }
  }

  sealed class Config : Parcelable {
    @Parcelize
    data class Marks(val data: ResearchDataModel) : Config()

    @Parcelize
    object None : Config()
  }
}