package components.models.shape

interface ScreenShape {
  val markId: Int
  val screenX: Double
  val screenY: Double
  val screenWidth: Double
  val screenHeight: Double
  val color: String
  val highlight: Boolean
}

data class ScreenCircle(
  override val markId: Int,
  override val screenX: Double,
  override val screenY: Double,
  override val screenWidth: Double,
  override val screenHeight: Double,
  override val color: String,
  override val highlight: Boolean
): ScreenShape

data class ScreenRectangle(
  override val markId: Int,
  override val screenX: Double,
  override val screenY: Double,
  override val screenWidth: Double,
  override val screenHeight: Double,
  override val color: String,
  override val highlight: Boolean
): ScreenShape

data class ScreenEllipse(
  override val markId: Int,
  override val screenX: Double,
  override val screenY: Double,
  override val screenWidth: Double,
  override val screenHeight: Double,
  override val color: String,
  override val highlight: Boolean
): ScreenShape