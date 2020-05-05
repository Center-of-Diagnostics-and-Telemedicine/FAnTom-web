package client.newmvi.menu.mipvalue.binder

import client.newmvi.menu.mipvalue.store.MipValueStore
import client.newmvi.menu.mipvalue.view.MipValueView

object MipValueViewEventToIntentMapper {

  operator fun invoke(event: MipValueView.Event): MipValueStore.Intent =
    when (event) {
      is MipValueView.Event.ChangeMethod -> MipValueStore.Intent.ChangeValue(
        event.value
      )
    }
}