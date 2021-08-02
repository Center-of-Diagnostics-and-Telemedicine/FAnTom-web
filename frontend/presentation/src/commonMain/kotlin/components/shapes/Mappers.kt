package components.shapes

import components.shapes.Shapes.Input
import components.shapes.Shapes.Model
import store.shapes.ShapesStore.Intent
import store.shapes.MyShapesStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      sliceNumber = it.sliceNumber,
      position = it.position,
      shapes = it.shapes,
      hounsfield = it.hounsfield,
      cutType = it.cutType,
      plane = it.plane
    )
  }

//internal val inputToIntent: (Input) -> Intent = {
//  when (it){
//    is Input.Circle -> Intent.HandleCircle(it.circle)
//    is Input.Ellipse -> Intent.HandleEllipse(it.ellipse)
//    is Input.Rectangle -> Intent.HandleRectangle(it.rectangle)
//  }
//}