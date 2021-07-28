package model

data class MouseDown(
  val dicomX: Double,
  val dicomY: Double,
  val metaKey: Boolean,
  val button: Short,
  val shiftKey: Boolean,
  val altKey: Boolean
)

data class ScreenDimensionsModel(
  val screenWidth: Int,
  val screenHeight: Int,
  val top: Int,
  val left: Int,
  val verticalRatio: Double,
  val horizontalRatio: Double,
  val radiusRatio: Double
)

fun initialScreenDimensionsModel(): ScreenDimensionsModel =
  ScreenDimensionsModel(
    screenWidth = 0,
    screenHeight = 0,
    top = 0,
    left = 0,
    verticalRatio = 0.0,
    horizontalRatio = 0.0,
    radiusRatio = 0.0,
  )