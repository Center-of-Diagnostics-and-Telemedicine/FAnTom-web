package components.fourcutscontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cutcontainer.CutContainer
import components.fourcutscontainer.FourCutsContainer.Dependencies
import model.FourGridModel
import model.MyPlane
import model.ResearchDataModel
import repository.MyBrightnessRepository
import repository.MyMarksRepository
import repository.MyMipRepository
import repository.MyResearchRepository

interface FourCutsContainer {

  val topLeftRouterState: Value<RouterState<*, Child>>
  val topRightRouterState: Value<RouterState<*, Child>>
  val bottomLeftRouterState: Value<RouterState<*, Child>>
  val bottomRightRouterState: Value<RouterState<*, Child>>

  fun changeTopLeftCutType(plane: MyPlane)
  fun changeTopRightCutType(plane: MyPlane)
  fun changeBottomLeftCutType(plane: MyPlane)
  fun changeBottomRightCutType(plane: MyPlane)

//  data class Model(
//    val cutType: CutType
//  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val fourCutsContainerOutput: Consumer<Output>
    val researchRepository: MyResearchRepository
    val brightnessRepository: MyBrightnessRepository
    val marksRepository: MyMarksRepository
    val mipRepository: MyMipRepository
    val data: ResearchDataModel
    val gridModel: FourGridModel
    val researchId: Int
  }

  data class Child(
    val component: CutContainer
  )

  sealed class Output {
  }

}

@Suppress("FunctionName") // Factory function
fun FourCutsContainer(
  componentContext: ComponentContext,
  dependencies: Dependencies
): FourCutsContainer =
  FourCutsContainerComponent(
    componentContext = componentContext,
    dependencies = dependencies,
    cutContainerFactory = { childContext, plane, output ->
      CutContainer(componentContext = childContext,
        dependencies = object : CutContainer.Dependencies, Dependencies by dependencies {
          override val cutContainerOutput: Consumer<CutContainer.Output> = output
          override val plane: MyPlane = plane
        })
    }
  )