package components.fourcutscontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cutcontainer.CutContainer
import components.fourcutscontainer.FourCutsContainer.Dependencies
import model.CutType
import model.ResearchData
import repository.MyBrightnessRepository
import repository.MyMipRepository
import repository.ResearchRepository

interface FourCutsContainer {

  val topLeftRouterState: Value<RouterState<*, Child>>
  val topRightRouterState: Value<RouterState<*, Child>>
  val bottomLeftRouterState: Value<RouterState<*, Child>>
  val bottomRightRouterState: Value<RouterState<*, Child>>

  fun changeTopLeftCutType(cutType: CutType)
  fun changeTopRightCutType(cutType: CutType)
  fun changeBottomLeftCutType(cutType: CutType)
  fun changeBottomRightCutType(cutType: CutType)

//  data class Model(
//    val cutType: CutType
//  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val fourCutsContainerOutput: Consumer<Output>
    val researchRepository: ResearchRepository
    val brightnessRepository: MyBrightnessRepository
    val mipRepository: MyMipRepository
    val data: ResearchData
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
    cutContainerFactory = { childContext, cutType, output ->
      CutContainer(componentContext = childContext,
        dependencies = object : CutContainer.Dependencies, Dependencies by dependencies {
          override val cutContainerOutput: Consumer<CutContainer.Output> = output
          override val cutType: CutType = cutType
        })
    }
  )