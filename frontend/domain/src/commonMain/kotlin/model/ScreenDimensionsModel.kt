package model

data class MouseDown(
  val screenX: Double,
  val screenY: Double,
  val metaKey: Boolean,
  val button: Short,
  val shiftKey: Boolean,
  val altKey: Boolean
)

data class ScreenDimensionsModel(
  val originalScreenWidth: Int,
  val originalScreenHeight: Int,
  val calculatedScreenWidth: Double,
  val calculatedScreenHeight: Double,
  val top: Double,
  val left: Double,
  val verticalRatio: Double,
  val horizontalRatio: Double,
  val radiusRatio: Double
)

fun initialScreenDimensionsModel(): ScreenDimensionsModel =
  ScreenDimensionsModel(
    originalScreenWidth = 0,
    originalScreenHeight = 0,
    calculatedScreenWidth = 0.0,
    calculatedScreenHeight = 0.0,
    top = 0.0,
    left = 0.0,
    verticalRatio = 0.0,
    horizontalRatio = 0.0,
    radiusRatio = 0.0,
  )