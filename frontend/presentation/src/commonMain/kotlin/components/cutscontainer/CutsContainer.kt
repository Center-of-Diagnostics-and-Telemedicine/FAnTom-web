package components.cutscontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cut.Cut
import components.cutscontainer.CutsContainer.Dependencies
import model.ResearchData
import repository.ResearchRepository

interface CutsContainer {

  val model: Value<Model>

  data class Model(
    val a: Any
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val cutsContainerOutput: Consumer<Output>
    val researchRepository: ResearchRepository
    val data: ResearchData
    val researchId: Int
  }

  sealed class Child {
    data class Single(val component: Cut) : Child()
    data class TwoVertical(val leftComponent: Cut, val rightComponent: Cut) : Child()
    data class TwoHorizontal(val topComponent: Cut, val bottomComponent: Cut) : Child()
    data class Four(
      val topLeftComponent: Cut,
      val topRightComponent: Cut,
      val bottomLeftComponent: Cut,
      val bottomRightComponent: Cut,
    ) : Child()
  }

  sealed class Output {
  }
}

@Suppress("FunctionName") // Factory function
fun CutsContainer(componentContext: ComponentContext, dependencies: Dependencies): CutsContainer =
  CutsContainerComponent(
    componentContext = componentContext,
    dependencies = dependencies,
    cut = { childContext, output ->
      Cut(componentContext = childContext,
        dependencies = object : Cut.Dependencies, Dependencies by dependencies {
          override val cutOutput: Consumer<Cut.Output> = output
        })
    }
  )