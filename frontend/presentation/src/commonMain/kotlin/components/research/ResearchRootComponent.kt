package components.research

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.observe
import com.arkivanov.decompose.value.operator.map
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.asValue
import components.cutscontainer.CutsContainer
import components.getStore
import components.research.ResearchRoot.*
import components.researchmarks.ResearchMarks
import components.researchtools.ResearchTools
import model.ResearchData

internal class ResearchRootComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  tools: (ComponentContext, ResearchData, Consumer<ResearchTools.Output>) -> ResearchTools,
  marks: (ComponentContext, ResearchData, Consumer<ResearchMarks.Output>) -> ResearchMarks,
  cuts: (ComponentContext, ResearchData, Consumer<CutsContainer.Output>) -> CutsContainer,
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
    ResearchToolsRouter(
      routerFactory = this,
      toolsFactory = tools,
      toolsOutput = Consumer(::onToolsOutput)
    )

  override val toolsRouterState: Value<RouterState<*, ToolsChild>> = toolsRouter.state

  private val marksRouter =
    ResearchMarksRouter(
      routerFactory = this,
      marksFactory = marks,
      marksOutput = Consumer(::onMarksOutput)
    )

  override val marksRouterState: Value<RouterState<*, MarksChild>> = marksRouter.state

  private val cutsContainerRouter =
    ResearchCutsRouter(
      routerFactory = this,
      cutsFactory = cuts,
      cutsOutput = Consumer(::onCutsContainerOutput)
    )

  override val cutsContainerRouterState: Value<RouterState<*, CutsChild>> =
    cutsContainerRouter.state

  init {
    store.asValue().observe(lifecycle) {
      it.data?.let { dataNew ->
        marksRouter.update(dataNew)
        toolsRouter.update(dataNew)
        cutsContainerRouter.update(dataNew)
      }
    }
  }

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

  private fun onCutsContainerOutput(output: CutsContainer.Output) {
    when (output) {
      else -> throw NotImplementedError("onCutsContainerOutput not implemented $output")
    }
  }
}