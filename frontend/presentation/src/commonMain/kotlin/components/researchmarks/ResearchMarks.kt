package components.researchmarks

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.researchmarks.ResearchMarks.Dependencies
import model.MarkModel
import model.MarkTypeModel
import model.ResearchDataModel
import repository.MyMarksRepository
import repository.MyResearchRepository

interface ResearchMarks {

  val model: Value<Model>

  fun onSelectItem(id: Int)
  fun onChangeComment(id: Int, comment: String)
  fun onChangeMarkType(id: Int, typeId: String)
  fun onChangeVisibility(id: Int)
  fun onDeleteItem(id: Int)

  data class Model(
    val error: String,
    val loading: Boolean,
    val currentMark: MarkModel?,
    val marks: List<MarkModel>,
    val markTypes: List<MarkTypeModel>
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val researchRepository: MyResearchRepository
    val marksRepository: MyMarksRepository
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