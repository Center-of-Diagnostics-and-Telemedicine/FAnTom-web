package client.newmvi.cut.binder

import client.newmvi.cut.store.CutStore
import client.newmvi.cut.view.CutView

internal object CutStateToViewModelMapper {

  operator fun invoke(state: CutStore.State): CutView.CutViewModel =
    CutView.CutViewModel(
      isLoading = state.isLoading,
      error = state.error,
      url = state.url
    )
}