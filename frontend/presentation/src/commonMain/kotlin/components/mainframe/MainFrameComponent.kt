package components.mainframe

import com.arkivanov.decompose.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.listroot.ListRoot
import components.mainframe.MainFrame.Child
import components.mainframe.MainFrame.Dependencies
import components.research.ResearchRoot

internal class MainFrameComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  private val researchRoot: (ComponentContext, researchId: Int, Consumer<ResearchRoot.Output>) -> ResearchRoot,
  private val list: (ComponentContext, Consumer<ListRoot.Output>) -> ListRoot,
) : MainFrame, ComponentContext by componentContext, Dependencies by dependencies {

  private val router =
    router<Configuration, Child>(
      initialConfiguration = Configuration.List,
      handleBackButton = true,
      childFactory = ::createChild
    )
  override val routerState: Value<RouterState<*, Child>> = router.state

  private fun createChild(configuration: Configuration, componentContext: ComponentContext): Child =
    when (configuration) {
      is Configuration.List -> Child.List(list(componentContext, Consumer(::onListOutput)))
      is Configuration.Research ->
        Child.Research(
          researchRoot(componentContext, configuration.researchId, Consumer(::onResearchOutput))
        )
    }

  private fun onListOutput(output: ListRoot.Output) {
    when (output) {
      is ListRoot.Output.NavigateToResearch ->
        router.push(Configuration.Research(output.researchId))
    }
  }

  private fun onResearchOutput(output: ResearchRoot.Output) {
    when (output) {
      ResearchRoot.Output.Back -> router.pop()
      else -> throw NotImplementedError("onResearchOutput not impelmented $output")
    }
  }

  private sealed class Configuration : Parcelable {
    @Parcelize
    class Research(val researchId: Int) : Configuration()

    @Parcelize
    object List : Configuration()
  }
}