package components.research

import components.research.ResearchRoot.Model
import model.ResearchDataModel
import store.research.ResearchStore.Label
import store.research.ResearchStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      error = it.error,
      loading = it.loading,
      data = it.data
    )
  }

internal val labelToRouters: (Label) -> ResearchDataModel? =
  {
    when (it) {
      is Label.ResearchData -> it.data
      Label.Back -> null
    }
  }