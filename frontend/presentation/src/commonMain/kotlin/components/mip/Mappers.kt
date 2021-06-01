package components.mip

import components.mip.Mip.Model
import store.tools.MipStore.State


internal val stateToModel: (State) -> Model =
  {
    Model(
      items = it.list,
      current = it.current,
      currentValue = it.currentValue
    )
  }