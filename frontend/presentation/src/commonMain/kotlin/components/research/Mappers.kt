package components.research

import components.research.Research.Model
import components.research.Research.Output
import store.research.ResearchStore.Label
import store.research.ResearchStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      error = it.error,
      loading = it.loading
    )
  }

internal val labelsToOutput: (Label) -> Output =
  {
    when (it) {
      Label.Back -> TODO()
    }
  }