package model

data class ExportedMarkModel(
  val id: Int,
  val diameterMm: Double,
  val type: String,
  val version: Double,
  val x: Double,
  val y: Double,
  val z: Double,
  val zType: String,
  val expertDecision: String?,
  val expertDecisionId: String?,
  val expertDecisionComment: String?,
  val expertDecisionMachineLearning: Boolean?,
  val expertDecisionProperSize: Boolean?,
  val expertDecisionType: String?
)