package components.shapes

import components.shapes.Shapes.Model
import store.shapes.ShapesStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      horizontalCoefficient = it.horizontalCoefficient,
      verticalCoefficient = it.verticalCoefficient,
      sliceNumber = it.sliceNumber,
      position = it.position,
      shapes = it.shapes,
      rects = it.rects,
      hounsfield = it.hounsfield,
      marks = it.marks,
      expertMarks = it.expertMarks,
      moveRect = it.moveRect,
    )
  }