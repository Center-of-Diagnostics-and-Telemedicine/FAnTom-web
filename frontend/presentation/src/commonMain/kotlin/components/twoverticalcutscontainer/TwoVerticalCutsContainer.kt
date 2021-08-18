package components.twoverticalcutscontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cutcontainer.CutContainer
import components.twoverticalcutscontainer.TwoVerticalCutsContainer.Dependencies
import model.MyPlane
import model.ResearchDataModel
import model.TwoVerticalGridModel
import repository.MyBrightnessRepository
import repository.MyMarksRepository
import repository.MyMipRepository
import repository.MyResearchRepository

interface TwoVerticalCutsContainer {

  val topRouterState: Value<RouterState<*, Child>>
  val bottomRouterState: Value<RouterState<*, Child>>

  fun changeTopCutType(plane: MyPlane)
  fun changeBottomCutType(plane: MyPlane)

  interface Dependencies {
    val storeFactory: StoreFactory
    val twoVerticalCutsContainerOutput: Consumer<Output>
    val researchRepository: MyResearchRepository
    val brightnessRepository: MyBrightnessRepository
    val marksRepository: MyMarksRepository
    val mipRepository: MyMipRepository
    val data: ResearchDataModel
    val gridModel: TwoVerticalGridModel
    val researchId: Int
  }

  data class Child(
    val component: CutContainer,
  )

  sealed class Output {
  }
}

@Suppress("FunctionName") // Factory function
fun TwoVerticalCutsContainer(
  componentContext: ComponentContext,
  dependencies: Dependencies
): TwoVerticalCutsContainer =
  TwoVerticalCutsContainerComponent(
    componentContext = componentContext,
    dependencies = dependencies,
    cutContainerFactory = { childContext, plane, output ->
      CutContainer(componentContext = childContext,
        dependencies = object : CutContainer.Dependencies, Dependencies by dependencies {
          override val cutContainerOutput: Consumer<CutContainer.Output> = output
          override val plane: MyPlane = plane
        })
    },
  )