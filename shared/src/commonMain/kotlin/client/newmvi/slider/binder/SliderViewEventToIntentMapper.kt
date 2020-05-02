package client.newmvi.slider.binder

import client.newmvi.slider.store.SliderStore
import client.newmvi.slider.view.SliderView

internal object SliderViewEventToIntentMapper {

  operator fun invoke(event: SliderView.Event): SliderStore.Intent =
    when (event) {
      is SliderView.Event.Drag -> SliderStore.Intent.Drag(event.value)
    }
}