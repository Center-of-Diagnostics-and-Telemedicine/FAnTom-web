package components.cutscontainer

import components.cutscontainer.CutsContainer.Model
import model.GridType
import store.gridcontainer.MyCutsContainerStore.Label
import store.gridcontainer.MyCutsContainerStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(gridType = it.gridType)
  }

internal val labelToChangeGrid: (Label) -> GridType? =
  {
    when(it){
      is Label.GridTypeChanged -> it.gridType
    }
  }