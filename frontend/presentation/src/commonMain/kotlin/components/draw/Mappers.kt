package components.draw

import components.draw.Draw.Model
import store.draw.DrawStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      startDicomX = it.startDicomX,
      startDicomY = it.startDicomY,
      dicomRadiusHorizontal = it.dicomRadiusHorizontal,
      dicomRadiusVertical = it.dicomRadiusVertical,
      isDrawingEllipse = it.isDrawingEllipse,
      isDrawingRectangle = it.isDrawingRectangle,
      isMoving = it.isMoving,
      isContrastBrightness = it.isContrastBrightness,
      cutType = it.cutType
    )
  }