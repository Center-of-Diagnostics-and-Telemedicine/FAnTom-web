package components.cutscontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cutscontainer.CutsContainer.Dependencies
import components.fourcutscontainer.FourCutsContainer
import components.singlecutcontainer.SingleCutContainer
import components.twohorizontalcutscontainer.TwoHorizontalCutsContainer
import components.twoverticalcutscontainer.TwoVerticalCutsContainer
import model.GridType
import model.ResearchData
import repository.GridRepository
import repository.ResearchRepository

interface CutsContainer {

  val model: Value<Model>

  val routerState: Value<RouterState<*, Child>>

  fun changeGrid(gridType: GridType)

  data class Model(
    val gridType: GridType
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val cutsContainerOutput: Consumer<Output>
    val researchRepository: ResearchRepository
    val gridRepository: GridRepository
    val data: ResearchData
    val researchId: Int
  }

  sealed class Child {
    data class Single(val component: SingleCutContainer) : Child()
    data class TwoVertical(val component: TwoVerticalCutsContainer) : Child()
    data class TwoHorizontal(val component: TwoHorizontalCutsContainer) : Child()
    data class Four(val component: FourCutsContainer) : Child()
  }

  sealed class Output {
  }
}

@Suppress("FunctionName") // Factory function
fun CutsContainer(componentContext: ComponentContext, dependencies: Dependencies): CutsContainer =
  CutsContainerComponent(
    componentContext = componentContext,
    dependencies = dependencies,
    singleCutContainerFactory = { childContext, output ->
      SingleCutContainer(
        componentContext = childContext,
        dependencies = object : SingleCutContainer.Dependencies, Dependencies by dependencies {
          override val singleCutContainerOutput: Consumer<SingleCutContainer.Output> = output
        })
    },
    twoVerticalCutsContainerFactory = { childContext, output ->
      TwoVerticalCutsContainer(
        componentContext = childContext,
        dependencies = object : TwoVerticalCutsContainer.Dependencies, Dependencies by dependencies {
          override val twoVerticalCutsContainerOutput: Consumer<TwoVerticalCutsContainer.Output> = output
        })
    },
    twoHorizontalCutsContainerFactory = {childContext, output ->
      TwoHorizontalCutsContainer(
        componentContext = childContext,
        dependencies = object : TwoHorizontalCutsContainer.Dependencies, Dependencies by dependencies {
          override val twoHorizontalCutsContainerOutput: Consumer<TwoHorizontalCutsContainer.Output> = output
        })
    },
    fourCutsContainerFactory = {childContext, output ->
      FourCutsContainer(
        componentContext = childContext,
        dependencies = object : FourCutsContainer.Dependencies, Dependencies by dependencies {
          override val fourCutsContainerOutput: Consumer<FourCutsContainer.Output> = output
        })
    }
  )