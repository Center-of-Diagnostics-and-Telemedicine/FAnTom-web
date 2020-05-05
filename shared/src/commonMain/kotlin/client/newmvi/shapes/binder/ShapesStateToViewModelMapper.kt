package client.newmvi.shapes.binder

import client.newmvi.shapes.store.ShapesStore
import client.newmvi.shapes.view.ShapesView

object ShapesStateToViewModelMapper {

  operator fun invoke(state: ShapesStore.State): ShapesView.ShapesViewModel {
    return ShapesView.ShapesViewModel(
      areas = state.areas,
      horizontal = state.horizontalLine,
      vertical = state.verticalLine,
      positionData = state.positionData,
      cubeColor = state.cubeColor,
      sliceNumber = state.sliceNumber,
      moveRects = state.moveRects,
      huValue = state.huValue
    )
  }
}