package model

data class ExpertMarkModel(
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

fun ExpertMarkModel.toExpertMarkEntity(): ExpertMarkEntity =
  ExpertMarkEntity(
    id = id,
    roiId = roiId,
    xCenter = xCenter,
    yCenter = yCenter,
    xSize = xSize,
    ySize = ySize,
    expertDecision = expertDecision,
    expertDecisionId = expertDecisionId,
    expertDecisionComment = expertDecisionComment,
    expertDecisionMachineLearning = expertDecisionMachineLearning,
    expertDecisionProperSize = expertDecisionProperSize,
    expertDecisionType = expertDecisionType,
  )

fun ExpertMarkEntity.toExpertMarkModel(): ExpertMarkModel =
  ExpertMarkModel(
    id = id,
    roiId = roiId,
    xCenter = xCenter,
    yCenter = yCenter,
    xSize = xSize,
    ySize = ySize,
    expertDecision = expertDecision,
    expertDecisionId = expertDecisionId,
    expertDecisionComment = expertDecisionComment,
    expertDecisionMachineLearning = expertDecisionMachineLearning,
    expertDecisionProperSize = expertDecisionProperSize,
    expertDecisionType = expertDecisionType,
  )