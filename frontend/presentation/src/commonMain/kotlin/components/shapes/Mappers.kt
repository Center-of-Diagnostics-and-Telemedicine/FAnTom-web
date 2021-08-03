package components.shapes

import components.models.shape.toScreenShape
import components.shapes.Shapes.Input
import components.shapes.Shapes.Model
import store.shapes.MyShapesStore.Intent
import store.shapes.MyShapesStore.State

internal val stateToModel: (State) -> Model =
  { state ->
    Model(
      sliceNumber = state.sliceNumber,
      position = state.position,
      shapes = state.shapes.map { it.toScreenShape(state.screenDimensionsModel) },
      hounsfield = state.hounsfield,
      cutType = state.cutType,
      plane = state.plane,
      screenDimensionsModel = state.screenDimensionsModel
    )
  }

internal val inputToIntent: (Input) -> Intent =
  {
    when (it) {
      is Input.Shapes -> Intent.HandleShapes(it.shapes)
      is Input.ScreenDimensionsChanged -> Intent.UpdateScreenDimensions(it.dimensions)
    }
  }