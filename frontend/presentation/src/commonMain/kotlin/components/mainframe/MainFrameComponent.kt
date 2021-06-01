package components.mainframe

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.listroot.ListRoot
import components.mainframe.MainFrame.Child
import components.research.Research

internal class MainFrameComponent(
  componentContext: ComponentContext,
  private val research: (ComponentContext, researchId: Int, Consumer<Research.Output>) -> Research,
  private val list: (ComponentContext, Consumer<ListRoot.Output>) -> ListRoot,
) : MainFrame, ComponentContext by componentContext {

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
          research(componentContext, configuration.researchId, Consumer(::onResearchOutput))
        )
    }

  private fun onListOutput(any: Any) {
    TODO("Not yet implemented")
  }

  private fun onResearchOutput(any: Any) {
    TODO("Not yet implemented")
  }

  private sealed class Configuration : Parcelable {
    //    @Parcelize
    class Research(val researchId: Int) : Configuration()

    //    @Parcelize
    object List : Configuration()
  }
}