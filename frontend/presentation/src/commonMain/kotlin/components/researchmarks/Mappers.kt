package components.researchmarks

import components.researchmarks.ResearchMarks.Model
import store.marks.MarksStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      error = it.error,
      loading = it.loading
    )
  }