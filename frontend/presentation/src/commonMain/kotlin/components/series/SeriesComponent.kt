package components.series

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import components.asValue
import components.getStore
import components.series.Series.Dependencies
import store.tools.SeriesStore

class SeriesComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
) : Series, ComponentContext by componentContext, Dependencies by dependencies {

  private val store = instanceKeeper.getStore {
    SeriesStoreProvider(
      storeFactory = storeFactory,
      researchId = researchId,
      seriesRepository = seriesRepository,
      dataModel = data
    ).provide()
  }

  override val model: Value<Series.Model> = store.asValue().map(stateToModel)

  override fun changeSeries(seriesName: String) {
    store.accept(SeriesStore.Intent.ChangeSeries(seriesName))
  }
}