package components.researchtools

import components.researchtools.ResearchTools.Model
import store.tools.ToolsStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      current = it.current,
      list = it.list
    )
  }