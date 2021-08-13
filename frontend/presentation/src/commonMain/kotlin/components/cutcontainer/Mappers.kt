package components.cutcontainer

import components.cut.Cut
import components.draw.Draw
import components.shapes.Shapes
import store.cut.CutContainerStore.Label

//internal val stateToModel: (State) -> Model =
//  {
//    Model(
//      sliceNumber = it.sliceNumber,
//      slice = it.slice,
//      black = it.black,
//      white = it.white,
//      gamma = it.gamma,
//      mipMethod = it.mipMethod,
//      mipValue = it.mipValue,
//      mainLoading = it.mainLoading,
//      secondaryLoading = it.secondaryLoading,
//      error = it.error,
//    )
//  }

internal val labelsToShapesInput: (Label) -> Shapes.Input? =
  {
    when (it) {
      is Label.CutModelChanged -> null
      is Label.Shapes -> Shapes.Input.Shapes(it.shapes)
      is Label.MousePosition -> Shapes.Input.MousePosition(it.pointPosition)
      is Label.ScreenDimensions -> Shapes.Input.ScreenDimensionsChanged(it.dimensions)
    }
  }

internal val labelsToCutInput: (Label) -> Cut.Input? =
  {
    when (it) {
      is Label.CutModelChanged -> Cut.Input.ChangeCutModel(it.cutModel)
      is Label.Shapes -> null
      is Label.MousePosition -> null
      is Label.ScreenDimensions -> null
    }
  }

internal val labelsToDrawInput: (Label) -> Draw.Input? =
  {
    when (it) {
      is Label.CutModelChanged -> null
      is Label.Shapes -> null
      is Label.MousePosition -> null
      is Label.ScreenDimensions -> Draw.Input.ScreenDimensionsChanged(it.dimensions)
    }
  }