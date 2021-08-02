package components.researchmarks

import components.researchmarks.ResearchMarks.Model
import store.marks.MyMarksStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      error = it.error,
      loading = it.loading,
      currentMark = it.currentMark,
      marks = it.marks
    )
  }