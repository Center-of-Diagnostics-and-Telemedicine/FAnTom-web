package model

interface PointPosition {
  val x: Double
  val y: Double
}

data class MultiPlanarPointPosition(
  override val x: Double,
  override val y: Double,
  val z: Double
) : PointPosition

data class PlanarPointPosition(
  override val x: Double,
  override val y: Double
) : PointPosition

data class MousePositionModel(
  override val x: Double,
  override val y: Double
) : PointPosition
