package components.cutscontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cutscontainer.CutsContainer.Dependencies
import components.fourcutscontainer.FourCutsContainer
import components.singlecutcontainer.SingleCutContainer
import components.threecutscontainer.ThreeCutsContainer
import components.twohorizontalcutscontainer.TwoHorizontalCutsContainer
import components.twoverticalcutscontainer.TwoVerticalCutsContainer
import model.*
import repository.*

interface CutsContainer {

  val model: Value<Model>

  val routerState: Value<RouterState<*, Child>>

  fun changeGrid(grid: MyNewGrid)

  data class Model(
    val grid: MyNewGrid?
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val cutsContainerOutput: Consumer<Output>
    val researchRepository: MyResearchRepository
    val gridRepository: GridRepository
    val brightnessRepository: MyBrightnessRepository
    val mipRepository: MyMipRepository
    val marksRepository: MyMarksRepository
    val data: ResearchDataModel
    val researchId: Int
  }

  sealed class Child {
    data class Single(val component: SingleCutContainer) : Child()
    data class TwoVertical(val component: TwoVerticalCutsContainer) : Child()
    data class TwoHorizontal(val component: TwoHorizontalCutsContainer) : Child()
    data class Three(val component: ThreeCutsContainer) : Child()
    data class Four(val component: FourCutsContainer) : Child()
    object None : Child()
  }

  sealed class Output {
  }
}

@Suppress("FunctionName") // Factory function
fun CutsContainer(componentContext: ComponentContext, dependencies: Dependencies): CutsContainer =
  CutsContainerComponent(
    componentContext = componentContext,
    dependencies = dependencies,
    singleCutContainerFactory = { childContext, grid, output ->
      SingleCutContainer(
        componentContext = childContext,
        dependencies = object : SingleCutContainer.Dependencies, Dependencies by dependencies {
          override val singleCutContainerOutput: Consumer<SingleCutContainer.Output> = output
          override val grid: SingleGridModel = grid as SingleGridModel
        })
    },
    twoVerticalCutsContainerFactory = { childContext, grid, output ->
      TwoVerticalCutsContainer(
        componentContext = childContext,
        dependencies = object : TwoVerticalCutsContainer.Dependencies,
          Dependencies by dependencies {
          override val twoVerticalCutsContainerOutput: Consumer<TwoVerticalCutsContainer.Output> =
            output
          override val gridModel: TwoVerticalGridModel = grid as TwoVerticalGridModel
        })
    },
    twoHorizontalCutsContainerFactory = { childContext, grid, output ->
      TwoHorizontalCutsContainer(
        componentContext = childContext,
        dependencies = object : TwoHorizontalCutsContainer.Dependencies,
          Dependencies by dependencies {
          override val twoHorizontalCutsContainerOutput: Consumer<TwoHorizontalCutsContainer.Output> =
            output
          override val gridModel: TwoHorizontalGridModel = grid as TwoHorizontalGridModel
        })
    },
    threeCutsContainerFactory = { childContext, grid, output ->
      ThreeCutsContainer(
        componentContext = childContext,
        dependencies = object : ThreeCutsContainer.Dependencies, Dependencies by dependencies {
          override val threeCutsContainerOutput: Consumer<ThreeCutsContainer.Output> = output
          override val gridModel: ThreeGridModel = grid as ThreeGridModel
        })
    },
    fourCutsContainerFactory = { childContext, grid, output ->
      FourCutsContainer(
        componentContext = childContext,
        dependencies = object : FourCutsContainer.Dependencies, Dependencies by dependencies {
          override val fourCutsContainerOutput: Consumer<FourCutsContainer.Output> = output
          override val gridModel: FourGridModel = grid as FourGridModel
        })
    }
  )