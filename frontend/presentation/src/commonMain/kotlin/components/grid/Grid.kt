package components.grid

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.grid.Grid.Dependencies
import model.GridType
import model.ResearchData
import repository.GridRepository

interface Grid {

  val model: Value<Model>

  fun changeGrid(gridType: GridType)

  data class Model(
    val grid: GridType,
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val gridRepository: GridRepository
    val gridOutput: Consumer<Output>
    val data: ResearchData
    val researchId: Int
  }

  sealed class Output {
  }

}

@Suppress("FunctionName") // Factory function
fun Grid(componentContext: ComponentContext, dependencies: Dependencies): Grid =
  GridComponent(componentContext, dependencies)