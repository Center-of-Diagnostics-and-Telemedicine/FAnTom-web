package components.cut

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.cut.Cut.Dependencies
import model.CutType
import repository.MyBrightnessRepository
import repository.MyMipRepository
import repository.ResearchRepository

interface Cut {

  val model: Value<Model>

  data class Model(
    val slice: String,
    val loading: Boolean,
    val error: String
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val brightnessRepository: MyBrightnessRepository
    val researchRepository: ResearchRepository
    val mipRepository: MyMipRepository
    val cutType: CutType
    val cutOutput: Consumer<Output>
    val researchId: Int
  }

  sealed class Output {
  }
}

@Suppress("FunctionName") // Factory function
fun Cut(componentContext: ComponentContext, dependencies: Dependencies): Cut =
  CutComponent(componentContext, dependencies)