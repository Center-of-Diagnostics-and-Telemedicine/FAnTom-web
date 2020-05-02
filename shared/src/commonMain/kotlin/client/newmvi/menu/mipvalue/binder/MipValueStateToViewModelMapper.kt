package client.newmvi.menu.mipvalue.binder

import client.newmvi.menu.mipvalue.store.MipValueStore
import client.newmvi.menu.mipvalue.view.MipValueView

internal object MipValueStateToViewModelMapper {

  operator fun invoke(state: MipValueStore.State): MipValueView.MipValueViewModel =
    MipValueView.MipValueViewModel(
      isLoading = state.isLoading,
      error = state.error,
      value = state.value,
      available = state.available
    )
}