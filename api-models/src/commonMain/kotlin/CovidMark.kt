package model

import kotlinx.serialization.Serializable


data class CovidMarkEntity(
  val userId: Int,
  val researchId: Int,
  val covidMarkData: CovidMarkData
)

@Serializable
data class CovidMarkData(
  val rightUpperLobe: LungLobe,
  val middleLobe: LungLobe,
  val rightLowerLobe: LungLobe,
  val leftUpperLobe: LungLobe,
  val leftLowerLobe: LungLobe
)

@Serializable
data class LungLobe(
  val name: String,
  val description: String,
  val shortName: String
)