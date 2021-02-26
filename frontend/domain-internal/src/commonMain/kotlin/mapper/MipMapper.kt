package mapper

import store.tools.MipStore.Intent
import store.tools.MipStore.State
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
