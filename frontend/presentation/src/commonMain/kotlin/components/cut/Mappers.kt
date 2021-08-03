package components.cut

import components.cut.Cut.Input
import components.cut.Cut.Model
import store.cut.MyCutStore.Intent
import store.cut.MyCutStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      slice = it.slice,
      loading = it.loading,
      error = it.error,
    )
  }

internal val inputToIntent: (Input) -> Intent = {
  when (it){
    is Input.ChangeCutModel -> Intent.HandleChangeCutModel(it.cutModel)
  }
}