package client.newmvi.menu.white.binder

import client.newmvi.menu.white.store.WhiteStore
import client.newmvi.menu.white.view.WhiteView

internal object WhiteStateToViewModelMapper {

  operator fun invoke(state: WhiteStore.State): WhiteView.WhiteViewModel =
    WhiteView.WhiteViewModel(
      isLoading = state.isLoading,
      error = state.error,
      value = state.value
    )
}