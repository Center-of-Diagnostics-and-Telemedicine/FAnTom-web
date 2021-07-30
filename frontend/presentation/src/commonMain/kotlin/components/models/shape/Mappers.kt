package components.models.shape

import components.draw.mapDicomRadiusToScreenRadius
import components.draw.mapDicomXToScreenX
import components.draw.mapDicomYToScreenY
import model.*

fun Shape.toScreenShape(dimensions: ScreenDimensionsModel): ScreenShape {
  return when (this) {
    is CircleModel -> mapToScreenCircle(dimensions)
    is RectangleModel -> mapToScreenRectangle(dimensions)
    is EllipseModel -> mapToScreenEllipse(dimensions)
    else -> throw NotImplementedError("toScreenShape not implemented shape $this")
  }
}

fun CircleModel.mapToScreenCircle(dimensions: ScreenDimensionsModel): ScreenCircle {
  val radius = mapDicomRadiusToScreenRadius(dicomWidth, dimensions.radiusRatio)
  return ScreenCircle(
    markId = id,
    screenX = mapDicomXToScreenX(dicomX, dimensions.horizontalRatio),
    screenY = mapDicomYToScreenY(dicomY, dimensions.verticalRatio),
    screenWidth = radius,
    screenHeight = radius,
    color = color,
    highlight = highlight,
  )
}

fun RectangleModel.mapToScreenRectangle(dimensions: ScreenDimensionsModel): ScreenRectangle =
  ScreenRectangle(
    markId = id,
    screenX = mapDicomXToScreenX(dicomX, dimensions.horizontalRatio),
    screenY = mapDicomYToScreenY(dicomY, dimensions.verticalRatio),
    screenWidth = mapDicomRadiusToScreenRadius(dicomWidth, dimensions.horizontalRatio),
    screenHeight = mapDicomRadiusToScreenRadius(dicomHeight, dimensions.verticalRatio),
    color = color,
    highlight = highlight,
  )

fun EllipseModel.mapToScreenEllipse(dimensions: ScreenDimensionsModel): ScreenEllipse =
  ScreenEllipse(
    markId = id,
    screenX = mapDicomXToScreenX(dicomX, dimensions.horizontalRatio),
    screenY = mapDicomYToScreenY(dicomY, dimensions.verticalRatio),
    screenWidth = mapDicomRadiusToScreenRadius(dicomWidth, dimensions.horizontalRatio),
    screenHeight = mapDicomRadiusToScreenRadius(dicomHeight, dimensions.verticalRatio),
    color = color,
    highlight = highlight,
  )