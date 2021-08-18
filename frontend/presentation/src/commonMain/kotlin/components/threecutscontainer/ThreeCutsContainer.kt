package components.threecutscontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cutcontainer.CutContainer
import components.threecutscontainer.ThreeCutsContainer.Dependencies
import model.MyPlane
import model.ResearchDataModel
import model.ThreeGridModel
import repository.MyBrightnessRepository
import repository.MyMarksRepository
import repository.MyMipRepository
import repository.MyResearchRepository

interface ThreeCutsContainer {

  val topLeftRouterState: Value<RouterState<*, Child>>
  val bottomLeftRouterState: Value<RouterState<*, Child>>
  val bottomRightRouterState: Value<RouterState<*, Child>>

  fun changeTopLeftCutType(plane: MyPlane)
  fun changeBottomLeftCutType(plane: MyPlane)
  fun changeBottomRightCutType(plane: MyPlane)

//  data class Model(
//    val cutType: CutType
//  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val threeCutsContainerOutput: Consumer<Output>
    val researchRepository: MyResearchRepository
    val brightnessRepository: MyBrightnessRepository
    val mipRepository: MyMipRepository
    val marksRepository: MyMarksRepository
    val data: ResearchDataModel
    val gridModel: ThreeGridModel
    val researchId: Int
  }

  data class Child(
    val component: CutContainer,
  )

  sealed class Output {
  }
}

@Suppress("FunctionName") // Factory function
fun ThreeCutsContainer(
  componentContext: ComponentContext,
  dependencies: Dependencies
): ThreeCutsContainer =
  ThreeCutsContainerComponent(
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