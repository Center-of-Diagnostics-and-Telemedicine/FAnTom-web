package components.listfilters

import components.listfilters.ListFilters.Model
import store.list.FilterStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      items = it.filters,
      current = it.currentFilter,
    )
  }