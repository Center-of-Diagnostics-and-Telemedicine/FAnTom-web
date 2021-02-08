package model

import kotlinx.serialization.Serializable


@Serializable
data class CovidMarkEntity(
  val covidMarkData: CovidMarkData
)

@Serializable
data class CovidMarkData(
  val rightUpperLobeValue: Int,
  val middleLobeValue: Int,
  val rightLowerLobeValue: Int,
  val leftUpperLobeValue: Int,
  val leftLowerLobeValue: Int
)