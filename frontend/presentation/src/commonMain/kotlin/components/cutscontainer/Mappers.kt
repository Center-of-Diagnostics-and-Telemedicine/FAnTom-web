package components.cutscontainer

import components.cutscontainer.CutsContainer.Model
import model.MyNewGrid
import store.gridcontainer.MyCutsContainerStore.Label
import store.gridcontainer.MyCutsContainerStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(grid = it.grid)
  }

internal val labelToChangeGrid: (Label) -> MyNewGrid? =
  {
    when(it){
      is Label.GridTypeChanged -> it.grid
    }
  }