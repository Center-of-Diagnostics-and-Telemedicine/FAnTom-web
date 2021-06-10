package components.twoverticalcutscontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cutcontainer.CutContainer
import components.twoverticalcutscontainer.TwoVerticalCutsContainer.Dependencies
import model.CutType
import model.ResearchData
import repository.MyBrightnessRepository
import repository.MyMipRepository
import repository.ResearchRepository

interface TwoVerticalCutsContainer {

  val topRouterState: Value<RouterState<*, Child>>
  val bottomRouterState: Value<RouterState<*, Child>>

  fun changeTopCutType(cutType: CutType)
  fun changeBottomCutType(cutType: CutType)

//  data class Model(
//    val cutType: CutType
//  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val twoVerticalCutsContainerOutput: Consumer<Output>
    val researchRepository: ResearchRepository
    val brightnessRepository: MyBrightnessRepository
    val mipRepository: MyMipRepository
    val data: ResearchData
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
    cutContainerFactory = { childContext, cutType, output ->
      CutContainer(componentContext = childContext,
        dependencies = object : CutContainer.Dependencies, Dependencies by dependencies {
          override val cutContainerOutput: Consumer<CutContainer.Output> = output
          override val cutType: CutType = cutType
        })
    },
  )