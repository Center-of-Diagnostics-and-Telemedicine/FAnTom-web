package components.grid

import components.grid.Grid.Model
import store.tools.MyGridStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      grid = it.grid,
      availableGrids = it.availableGrids
    )
  }