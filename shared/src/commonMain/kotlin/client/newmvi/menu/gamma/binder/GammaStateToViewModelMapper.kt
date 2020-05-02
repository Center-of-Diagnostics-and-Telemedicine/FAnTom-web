package client.newmvi.menu.gamma.binder

import client.newmvi.menu.gamma.store.GammaStore
import client.newmvi.menu.gamma.view.GammaView

internal object GammaStateToViewModelMapper {

  operator fun invoke(state: GammaStore.State): GammaView.GammaViewModel =
    GammaView.GammaViewModel(
      isLoading = state.isLoading,
      error = state.error,
      value = state.value
    )
}