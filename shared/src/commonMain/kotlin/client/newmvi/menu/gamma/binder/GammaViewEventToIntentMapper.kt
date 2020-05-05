package client.newmvi.menu.gamma.binder

import client.newmvi.menu.gamma.store.GammaStore
import client.newmvi.menu.gamma.view.GammaView

object GammaViewEventToIntentMapper {

  operator fun invoke(event: GammaView.Event): GammaStore.Intent =
    when (event) {
      is GammaView.Event.ChangeValue -> GammaStore.Intent.Drag(
        event.value
      )
    }
}