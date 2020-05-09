package client.newmvi.cut.binder

import client.newmvi.cut.store.CutStore
import client.newmvi.cut.view.CutView

object CutStateToViewModelMapper {

  operator fun invoke(state: CutStore.State): CutView.CutViewModel =
    CutView.CutViewModel(
      isLoading = state.isLoading,
      error = state.error,
      img = state.img
    )
}