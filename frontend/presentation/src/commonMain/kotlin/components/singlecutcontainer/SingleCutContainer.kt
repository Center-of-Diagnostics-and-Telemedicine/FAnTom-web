package components.singlecutcontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cutcontainer.CutContainer
import components.singlecutcontainer.SingleCutContainer.Dependencies
import model.CutType
import model.Plane
import model.ResearchDataModel
import model.buildPlane
import repository.MyBrightnessRepository
import repository.MyMipRepository
import repository.MyResearchRepository

interface SingleCutContainer {

  val routerState: Value<RouterState<*, Child>>

  fun changeCutType(cutType: CutType)

//  data class Model(
//    val cutType: CutType
//  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val singleCutContainerOutput: Consumer<Output>
    val researchRepository: MyResearchRepository
    val brightnessRepository: MyBrightnessRepository
    val mipRepository: MyMipRepository
    val data: ResearchDataModel
    val researchId: Int
  }

  data class Child(
    val component: CutContainer
  )

  sealed class Output {
  }
}

@Suppress("FunctionName") // Factory function
fun SingleCutContainer(
  componentContext: ComponentContext,
  dependencies: Dependencies
): SingleCutContainer =
  SingleCutContainerComponent(
    componentContext = componentContext,
    dependencies = dependencies,
    cutContainerFactory = { childContext, cutType, output ->
      CutContainer(componentContext = childContext,
        dependencies = object : CutContainer.Dependencies, Dependencies by dependencies {
          override val cutContainerOutput: Consumer<CutContainer.Output> = output
          override val cutType: CutType = cutType
          override val plane: Plane = buildPlane(cutType, data)
        })
    }
  )