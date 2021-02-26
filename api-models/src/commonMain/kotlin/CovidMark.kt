package model

import kotlinx.serialization.Serializable


@Serializable
data class CovidMarkEntity(
  val covidMarkData: CovidMarkData
)

@Serializable
data class CovidMarkData(
  val rightUpperLobeValue: Int,
  val rightLowerLobeValue: Int,
  val middleLobeValue: Int,
  val leftUpperLobeValue: Int,
  val leftLowerLobeValue: Int
)

fun getEmptyCovidMarkEntity(): CovidMarkEntity =
  CovidMarkEntity(
    covidMarkData = CovidMarkData(
      rightUpperLobeValue = noValue,
      rightLowerLobeValue = noValue,
      middleLobeValue = noValue,
      leftUpperLobeValue = noValue,
      leftLowerLobeValue = noValue,
    )
  )