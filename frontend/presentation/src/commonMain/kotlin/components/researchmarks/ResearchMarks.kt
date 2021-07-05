package components.researchmarks

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.researchmarks.ResearchMarks.Dependencies
import model.ResearchData
import model.ResearchDataModel
import repository.MarksRepository
import repository.ResearchRepository

interface ResearchMarks {

  val models: Value<Model>

  data class Model(
    val error: String,
    val loading: Boolean
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val researchRepository: ResearchRepository
    val marksRepository: MarksRepository
    val marksOutput: Consumer<Output>
    val researchId: Int
    val data: ResearchDataModel
  }

  sealed class Output {
    object Unauthorized : Output()
  }
}

@Suppress("FunctionName") // Factory function
fun ResearchMarks(componentContext: ComponentContext, dependencies: Dependencies): ResearchMarks =
  ResearchMarksComponent(componentContext, dependencies)