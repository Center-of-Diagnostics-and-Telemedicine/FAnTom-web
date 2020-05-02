package client.newmvi.cut.binder

import client.newmvi.cut.store.CutStore
import client.newmvi.cut.view.CutView

internal object CutViewEventToIntentMapper {

  operator fun invoke(event: CutView.Event): CutStore.Intent =
    when (event) {
      is CutView.Event.ErrorShown -> CutStore.Intent.DismissError
    }
}