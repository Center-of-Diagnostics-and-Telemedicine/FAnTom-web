package components.cutscontainer

import components.cutscontainer.CutsContainer.Model
import store.gridcontainer.MyCutsContainerStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(a = it.cuts)
  }