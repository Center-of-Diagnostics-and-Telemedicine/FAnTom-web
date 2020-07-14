package model

interface PointPosition

data class MPRPointPosition(
  val x: Double,
  val y: Double,
  val z: Double
) : PointPosition

data class PlanarPointPosition(
  val x: Double,
  val y: Double
): PointPosition
