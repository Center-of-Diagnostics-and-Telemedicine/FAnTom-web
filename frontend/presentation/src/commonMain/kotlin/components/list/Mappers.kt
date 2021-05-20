package components.list

import components.list.ResearchList.Model
import store.list.ListStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      items = it.list,
      error = it.error,
      loading = it.loading
    )
  }
