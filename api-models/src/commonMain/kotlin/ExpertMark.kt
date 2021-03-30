package model

import kotlinx.serialization.Serializable

@Serializable
data class ExpertMarkEntity(
  val id: Int,
  val roiId: Int,
  val xCenter: Double,
  val yCenter: Double,
  val xSize: Double,
  val ySize: Double,
  val expertDecision: Int?,
  val expertDecisionId: String?,
  val expertDecisionComment: String?,
  val expertDecisionMachineLearning: Boolean?,
  val expertDecisionProperSize: Boolean?,
  val expertDecisionType: Int?
)