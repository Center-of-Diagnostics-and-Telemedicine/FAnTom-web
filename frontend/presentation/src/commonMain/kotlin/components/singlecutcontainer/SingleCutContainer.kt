package components.singlecutcontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cut.Cut
import components.singlecutcontainer.SingleCutContainer.Dependencies
import model.CutType
import model.ResearchData
import repository.ResearchRepository

interface SingleCutContainer {

  val routerState: Value<RouterState<*, Child>>

  fun changeCutType(cutType: CutType)

  data class Model(
    val cutType: CutType
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val singleCutContainerOutput: Consumer<Output>
    val researchRepository: ResearchRepository
    val data: ResearchData
    val researchId: Int
  }

  data class Child(
    val component: Cut
  )

  sealed class Output {
  }
}

@Suppress("FunctionName") // Factory function
fun SingleCutContainer(
  componentContext: ComponentContext,
  dependencies: Dependencies
): SingleCutContainer =
  SingleCutContainerComponent(
    componentContext = componentContext,
    dependencies = dependencies,
    cutFactory = { childContext, cutType, output ->
      Cut(componentContext = childContext,
        dependencies = object : Cut.Dependencies, Dependencies by dependencies {
          override val cutOutput: Consumer<Cut.Output> = output
          override val cutType: CutType = cutType
        })
    }
  )