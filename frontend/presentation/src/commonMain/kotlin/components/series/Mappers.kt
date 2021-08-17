package components.series

import components.series.Series.Model
import store.tools.SeriesStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      currentSeries = it.currentSeries,
      series = it.series
    )
  }