package components.research

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.asValue
import components.getStore
import components.research.ResearchRoot.*
import components.researchmarks.ResearchMarks
import components.researchtools.ResearchTools

internal class ResearchRootComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  private val tools: (ComponentContext, Consumer<ResearchTools.Output>) -> ResearchTools,
  private val marks: (ComponentContext, Consumer<ResearchMarks.Output>) -> ResearchMarks,
) : ResearchRoot, ComponentContext by componentContext, Dependencies by dependencies {

  private val store =
    instanceKeeper.getStore {
      ResearchStoreProvider(
        storeFactory = storeFactory,
        repository = researchRepository,
        researchId = researchId
      ).provide()
    }

  override val models: Value<Model> = store.asValue().map(stateToModel)

  private val toolsRouter =
    router(
      initialConfiguration = Configuration.Tools,
      key = "ToolsRouter",
      childFactory = { conf, ctx -> Tools(tools(ctx, Consumer(::onToolsOutput))) }
    )

  override val toolsRouterState: Value<RouterState<*, Tools>> = toolsRouter.state

  private val marksRouter =
    router(
      initialConfiguration = Configuration.Marks,
      key = "MarksRouter",
      childFactory = { conf, ctx -> Marks(marks(ctx, Consumer(::onMarksOutput))) }
    )

  override val marksRouterState: Value<RouterState<*, Marks>> = marksRouter.state

  private fun onToolsOutput(output: ResearchTools.Output) {
    when (output) {
      ResearchTools.Output.Back -> researchOutput.onNext(Output.Back)
      else -> throw NotImplementedError("onToolsOutput not implemented $output")
    }
  }

  private fun onMarksOutput(output: ResearchMarks.Output) {
    when (output) {
      else -> throw NotImplementedError("onMarksOutput not implemented $output")
    }
  }

  private sealed class Configuration : Parcelable {
    //    @Parcelize
    object Tools : Configuration()

    //    @Parcelize
    object Marks : Configuration()
  }
}