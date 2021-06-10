package components.cut

import components.cut.Cut.Model
import store.cut.MyCutStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      slice = it.slice,
      loading = it.loading,
      error = it.error,
    )
  }