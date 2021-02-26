package model

interface PointPosition

data class MultiPlanarPointPosition(
  val x: Double,
  val y: Double,
  val z: Double
) : PointPosition

data class PlanarPointPosition(
  val x: Double,
  val y: Double
): PointPosition
