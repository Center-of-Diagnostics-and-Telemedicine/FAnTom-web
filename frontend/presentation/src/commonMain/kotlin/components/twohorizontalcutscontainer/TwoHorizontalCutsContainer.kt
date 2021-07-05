package components.twohorizontalcutscontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cutcontainer.CutContainer
import components.twohorizontalcutscontainer.TwoHorizontalCutsContainer.Dependencies
import model.CutType
import model.ResearchData
import model.ResearchDataModel
import repository.MyBrightnessRepository
import repository.MyMipRepository
import repository.ResearchRepository

interface TwoHorizontalCutsContainer {

  val leftRouterState: Value<RouterState<*, Child>>
  val rightRouterState: Value<RouterState<*, Child>>

  fun changeLeftCutType(cutType: CutType)
  fun changeRightCutType(cutType: CutType)

//  data class Model(
//    val cutType: CutType
//  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val twoHorizontalCutsContainerOutput: Consumer<Output>
    val researchRepository: ResearchRepository
    val brightnessRepository: MyBrightnessRepository
    val mipRepository: MyMipRepository
    val data: ResearchDataModel
    val researchId: Int
  }

  data class Child(
    val component: CutContainer,
  )

  sealed class Output {
  }
}

@Suppress("FunctionName") // Factory function
fun TwoHorizontalCutsContainer(
  componentContext: ComponentContext,
  dependencies: Dependencies
): TwoHorizontalCutsContainer =
  TwoHorizontalCutsContainerComponent(
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