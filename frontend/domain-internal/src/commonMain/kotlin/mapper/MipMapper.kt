package mapper

import store.MipStore.Intent
import store.MipStore.State
import  view.MipView.Event
import  view.MipView.Model

val mipStateToMipModel: State.() -> Model? = {
  Model(
    items = list,
    current = current,
    currentValue = currentValue
  )
}

val mipEventToMipIntent: Event.() -> Intent? =
  {
    when (this) {
      is Event.ItemClick -> Intent.HandleMipClick(mip)
      is Event.MipValueChanged -> Intent.HandleMipValueChanged(value)
    }
  }
