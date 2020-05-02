package client.newmvi.menu.black.binder

import client.newmvi.menu.black.store.BlackStore
import client.newmvi.menu.black.view.BlackView

internal object BlackStateToViewModelMapper {

  operator fun invoke(state: BlackStore.State): BlackView.BlackViewModel =
    BlackView.BlackViewModel(
      isLoading = state.isLoading,
      error = state.error,
      value = state.value
    )
}