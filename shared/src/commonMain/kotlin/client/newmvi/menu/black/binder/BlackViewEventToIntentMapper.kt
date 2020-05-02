package client.newmvi.menu.black.binder

import client.newmvi.menu.black.store.BlackStore
import client.newmvi.menu.black.view.BlackView

internal object BlackViewEventToIntentMapper {

  operator fun invoke(event: BlackView.Event): BlackStore.Intent =
    when (event) {
      is BlackView.Event.ChangeValue -> BlackStore.Intent.ChangeValue(
        event.value
      )
    }
}