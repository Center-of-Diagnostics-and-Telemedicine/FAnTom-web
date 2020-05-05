package client.newmvi.menu.white.binder

import client.newmvi.menu.white.store.WhiteStore
import client.newmvi.menu.white.view.WhiteView

object WhiteViewEventToIntentMapper {

  operator fun invoke(event: WhiteView.Event): WhiteStore.Intent =
    when (event) {
      is WhiteView.Event.ChangeValue -> WhiteStore.Intent.Drag(
        event.value
      )
    }
}