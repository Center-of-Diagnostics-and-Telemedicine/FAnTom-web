package components.cut

import components.cut.Cut.Model
import store.cut.CutStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      sliceNumber = it.sliceNumber,
      slice = it.slice,
      black = it.black,
      white = it.white,
      gamma = it.gamma,
      mipMethod = it.mipMethod,
      mipValue = it.mipValue,
      mainLoading = it.mainLoading,
      secondaryLoading = it.secondaryLoading,
      error = it.error,
    )
  }