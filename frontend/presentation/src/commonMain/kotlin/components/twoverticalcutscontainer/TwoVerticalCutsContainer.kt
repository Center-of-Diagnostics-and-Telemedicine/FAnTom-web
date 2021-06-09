package components.twoverticalcutscontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cut.Cut
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
    val component: Cut,
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
    cutFactory = { childContext, cutType, output ->
      Cut(componentContext = childContext,
        dependencies = object : Cut.Dependencies, Dependencies by dependencies {
          override val cutOutput: Consumer<Cut.Output> = output
          override val cutType: CutType = cutType
        })
    },

    )