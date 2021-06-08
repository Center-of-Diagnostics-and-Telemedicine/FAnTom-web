package components.research

import components.research.ResearchRoot.Model
import components.research.ResearchRoot.Output
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

internal val labelsToOutput: (Label) -> Output =
  {
    when (it) {
      Label.Back -> TODO()
    }
  }