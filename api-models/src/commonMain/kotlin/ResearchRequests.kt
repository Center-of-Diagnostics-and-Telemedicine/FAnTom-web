package model

import kotlinx.serialization.Serializable

@Serializable
data class SliceRequest(
  val black: Int,
  val white: Int,
  val gamma: Double,
  val sliceType: Int,
  val mipMethod: Int,
  val sliceNumber: Int,
  val mipValue: Int
)

@Serializable
data class HounsfieldRequest(
  val axialCoord: Int,
  val frontalCoord: Int,
  val sagittalCoord: Int
)

@Serializable
data class ConfirmCTTypeRequest(
  val researchId: Int,
  val ctType: Int,
  val leftPercent: Int,
  val rightPercent: Int
)
