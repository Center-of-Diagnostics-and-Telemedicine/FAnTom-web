package client.newmvi.slider.binder

import client.newmvi.slider.store.SliderStore
import client.newmvi.slider.view.SliderView

internal object SliderStateToViewModelMapper {

  operator fun invoke(state: SliderStore.State): SliderView.SliderViewModel =
    SliderView.SliderViewModel(
      isLoading = state.isLoading,
      error = state.error,
      value = state.value
    )
}