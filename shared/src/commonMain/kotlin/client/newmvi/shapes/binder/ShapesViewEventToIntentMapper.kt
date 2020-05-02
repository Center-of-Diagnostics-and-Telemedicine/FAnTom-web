package client.newmvi.shapes.binder

import client.newmvi.shapes.store.ShapesStore
import client.newmvi.shapes.view.ShapesView

internal object ShapesViewEventToIntentMapper {

  operator fun invoke(event: ShapesView.Event): ShapesStore.Intent =
    when (event) {
      ShapesView.Event.Idle -> ShapesStore.Intent.Idle
      is ShapesView.Event.ChangeCutType -> ShapesStore.Intent.ChangeCutType(event.type, event.cellModel)
    }
}