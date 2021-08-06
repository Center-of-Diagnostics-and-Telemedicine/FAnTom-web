package components.draw

import components.draw.Draw.*
import components.models.shape.toScreenShape
import model.*
import store.draw.MyDrawStore.*
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt

internal val stateToModel: (State) -> Model =
  {
    Model(
      shape = it.shape?.toScreenShape(it.screenDimensionsModel),
      cutType = it.cutType,
      plane = it.plane,
      screenDimensionsModel = it.screenDimensionsModel
    )
  }

internal val inputToIntent: (Input) -> Intent =
  {
    when (it) {
      is Input.ScreenDimensionsChanged -> Intent.UpdateScreenDimensions(it.dimensions)
    }
  }

internal val labelsToOutput: (Label) -> Output =
  {
    when (it) {
      is Label.CircleDrawn -> Output.Circle(it.circle)
      is Label.EllipseDrawn -> Output.Ellipse(it.ellipse)
      is Label.RectangleDrawn -> Output.Rectangle(it.rectangle)
      is Label.ChangeSlice -> Output.ChangeSlice(it.deltaDicomY)
      is Label.ChangeContrastBrightness -> Output.ChangeContrastBrightness(it.deltaX, it.deltaY)
      is Label.MousePointPosition -> Output.PointPosition(it.mousePosition)
      Label.OpenFullCut -> Output.OpenFullCut
      Label.ContrastBrightnessChanged -> TODO()
    }
  }

internal fun MouseDown.toIntent(dimensions: ScreenDimensionsModel): Intent {
  val isDrawEllipse = metaKey && button == LEFT_MOUSE_BUTTON
  val isDrawRectangle = shiftKey && button == LEFT_MOUSE_BUTTON
  val isContrastBrightness = button == MIDDLE_MOUSE_BUTTON
  val dicomX = mapScreenXToDicomX(screenX, dimensions.horizontalRatio)
  val dicomY = mapScreenYToDicomY(screenY, dimensions.verticalRatio)
  return when {
    isDrawEllipse -> Intent.StartDrawEllipse(startDicomX = dicomX, startDicomY = dicomY)
    isDrawRectangle -> Intent.StartDrawRectangle(startDicomX = dicomX, startDicomY = dicomY)
    isContrastBrightness -> Intent.StartContrastBrightness(
      startDicomX = dicomX,
      startDicomY = dicomY
    )
    else -> Intent.StartMouseClick(startDicomY = dicomY, startDicomX = dicomX)
  }
}

internal fun Plane.calculateScreenDimensions(
  screenHeight: Int,
  screenWidth: Int
): ScreenDimensionsModel {
  val screenWidthDouble = screenWidth.toDouble()
  val screenHeightDouble = screenHeight.toDouble()
  val resultWidth =
    calculateWidth(screenHeight = screenHeightDouble, screenWidth = screenWidthDouble)
  val resultHeight =
    calculateHeight(screenHeight = screenHeightDouble, screenWidth = screenWidthDouble)
  val resultTop = calculateTop(screenHeight = screenHeightDouble, resultHeight = resultHeight)
  val resultLeft = calculateLeft(screenWidth = screenWidthDouble, resultWidth = resultWidth)
  val verticalRatio = calculateVerticalRatio(resultHeight = resultHeight)
  val horizontalRatio = calculateHorizontalRatio(resultWidth = resultWidth)
  val radiusRatio = calculateRadiusRatio(resultHeight = resultHeight, resultWidth = resultWidth)
  return ScreenDimensionsModel(
    originalScreenWidth = screenWidth,
    originalScreenHeight = screenHeight,
    calculatedScreenWidth = resultWidth,
    calculatedScreenHeight = resultHeight,
    top = resultTop,
    left = resultLeft,
    verticalRatio = verticalRatio,
    horizontalRatio = horizontalRatio,
    radiusRatio = radiusRatio
  )
}

private fun Plane.calculateRadiusRatio(
  resultHeight: Double,
  resultWidth: Double
): Double {
  val dicomRadius =
    sqrt(data.screenSizeV.toDouble().pow(2) + data.screenSizeH.toDouble().pow(2))
  val screenRadius = sqrt(resultHeight.pow(2) + resultWidth.pow(2))
  return dicomRadius / screenRadius
}

private fun calculateTop(screenHeight: Double, resultHeight: Double): Double {
  val mTop = screenHeight - resultHeight
  return if (mTop <= 0) 0.0 else ceil(mTop / 2)
}

private fun calculateLeft(screenWidth: Double, resultWidth: Double): Double {
  val mLeft = screenWidth - resultWidth
  return if (mLeft <= 0) 0.0 else ceil(mLeft / 2)
}

private fun Plane.calculateWidth(screenHeight: Double, screenWidth: Double): Double {
  val dicomWidth = data.screenSizeH.toDouble()
  val dicomHeight = data.screenSizeV.toDouble()
  val ri = dicomWidth / dicomHeight
  val rs = screenWidth / screenHeight
  return if (rs > ri) {
    ceil(dicomWidth * screenHeight / dicomHeight)
  } else {
    screenWidth
  }
}

private fun Plane.calculateHeight(screenHeight: Double, screenWidth: Double): Double {
  val dicomWidth = data.screenSizeH.toDouble()
  val dicomHeight = data.screenSizeV.toDouble()
  val ri = dicomWidth / dicomHeight
  val rs = screenWidth / screenHeight
  return if (rs > ri) {
    screenHeight
  } else {
    ceil(dicomHeight * screenWidth / dicomWidth)
  }
}

private fun Plane.calculateVerticalRatio(resultHeight: Double): Double =
  data.screenSizeV.toDouble() / resultHeight

private fun Plane.calculateHorizontalRatio(resultWidth: Double): Double =
  data.screenSizeH.toDouble() / resultWidth

fun mapScreenXToDicomX(screenX: Double, horizontalRatio: Double): Double =
  screenX * horizontalRatio

fun mapScreenYToDicomY(screenY: Double, verticalRatio: Double): Double =
  screenY * verticalRatio

fun mapDicomXToScreenX(dicomX: Double, horizontalRatio: Double): Double =
  dicomX / horizontalRatio

fun mapDicomYToScreenY(dicomY: Double, verticalRatio: Double): Double =
  dicomY / verticalRatio

fun mapDicomRadiusToScreenRadius(dicomRadius: Double, screenRadius: Double): Double =
  dicomRadius / screenRadius
