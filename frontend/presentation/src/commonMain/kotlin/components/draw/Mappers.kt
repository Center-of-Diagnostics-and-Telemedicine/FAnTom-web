package components.draw

import components.draw.Draw.Model
import components.models.shape.toScreenShape
import model.*
import store.draw.MyDrawStore.Intent
import store.draw.MyDrawStore.State
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
  val resultWidth = calculateWidth(screenHeight = screenHeight, screenWidth = screenWidth)
  val resultHeight = calculateHeight(screenHeight = screenHeight, screenWidth = screenWidth)
  val resultTop = calculateTop(screenHeight = screenHeight, resultHeight = resultHeight)
  val resultLeft = calculateLeft(screenWidth = screenWidth, resultWidth = resultWidth)
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
  resultHeight: Int,
  resultWidth: Int
): Double {
  val dicomRadius =
    sqrt(data.screenSizeV.toDouble().pow(2) + data.screenSizeH.toDouble().pow(2))
  val screenRadius = sqrt(resultHeight.toDouble().pow(2) + resultWidth.toDouble().pow(2))
  return dicomRadius / screenRadius
}

private fun calculateTop(screenHeight: Int, resultHeight: Int): Int {
  val mTop = screenHeight - resultHeight
  return if (mTop <= 0) 0 else mTop / 2
}

private fun calculateLeft(screenWidth: Int, resultWidth: Int): Int {
  val mLeft = screenWidth - resultWidth
  return if (mLeft <= 0) 0 else mLeft / 2
}

private fun Plane.calculateWidth(screenHeight: Int, screenWidth: Int): Int {
  val dicomWidth = data.screenSizeH
  val dicomHeight = data.screenSizeV
  val ri = dicomWidth.toDouble() / dicomHeight
  val rs = screenWidth.toDouble() / screenHeight
  return if (rs > ri) {
    dicomWidth * screenHeight / dicomHeight
  } else {
    screenWidth
  }
}

private fun Plane.calculateHeight(screenHeight: Int, screenWidth: Int): Int {
  val dicomWidth = data.screenSizeH
  val dicomHeight = data.screenSizeV
  val ri = dicomWidth.toDouble() / dicomHeight
  val rs = screenWidth.toDouble() / screenHeight
  return if (rs > ri) {
    screenHeight
  } else {
    dicomHeight * screenHeight / dicomWidth
  }
}

private fun Plane.calculateVerticalRatio(resultHeight: Int) =
  data.screenSizeV.toDouble() / resultHeight

private fun Plane.calculateHorizontalRatio(resultWidth: Int) =
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
