package components.twohorizontalcutscontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cut.Cut
import components.twohorizontalcutscontainer.TwoHorizontalCutsContainer.Dependencies
import model.CutType
import model.ResearchData
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
    val data: ResearchData
    val researchId: Int
  }

  data class Child(
    val component: Cut,
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
    cutFactory = { childContext, cutType, output ->
      Cut(componentContext = childContext,
        dependencies = object : Cut.Dependencies, Dependencies by dependencies {
          override val cutOutput: Consumer<Cut.Output> = output
          override val cutType: CutType = cutType
        })
    },
  )