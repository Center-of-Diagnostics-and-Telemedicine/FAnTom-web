package components.grid

import components.grid.Grid.Model
import model.toGridType
import store.tools.MyGridStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      grid = it.grid.toGridType(),
      series = it.series,
      currentSeries = it.currentSeries
    )
  }