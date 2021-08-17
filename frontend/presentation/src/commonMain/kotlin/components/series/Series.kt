package components.series

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.series.Series.Dependencies
import model.ResearchDataModel
import model.SeriesModel
import repository.GridRepository
import repository.SeriesRepository

interface Series {

  val model: Value<Model>

  fun changeSeries(seriesName: String)

  data class Model(
    val series: Map<String, SeriesModel>,
    val currentSeries: SeriesModel
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val gridRepository: GridRepository
    val seriesOutput: Consumer<Output>
    val data: ResearchDataModel
    val seriesRepository: SeriesRepository
    val researchId: Int
  }

  sealed class Output {
  }

}

@Suppress("FunctionName") // Factory function
fun Series(componentContext: ComponentContext, dependencies: Dependencies): Series =
  SeriesComponent(componentContext, dependencies)