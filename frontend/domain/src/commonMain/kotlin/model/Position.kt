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

data class PointPositionModel(
  override val x: Double,
  override val y: Double
) : PointPosition

data class MouseClickPositionModel(
  val startX: Double,
  val startY: Double,
  override val x: Double = 0.0,
  override val y: Double = 0.0
) : PointPosition
