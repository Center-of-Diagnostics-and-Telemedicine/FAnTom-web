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
  val expertDecision: Int? = null,
  val expertDecisionId: String? = null,
  val expertDecisionComment: String? = null,
  val expertDecisionMachineLearning: Boolean? = null,
  val expertDecisionProperSize: Boolean? = null,
  val expertDecisionType: Int? = null
)

fun MarkModel.toEmptyExpertMarkEntity(roi: ExpertRoiEntity): ExpertMarkEntity =
  ExpertMarkEntity(
    id = -1,
    roiId = roi.id,
    xCenter = markData.x,
    yCenter = markData.y,
    xSize = markData.sizeHorizontal,
    ySize = markData.sizeVertical,
    expertDecision = null,
    expertDecisionId = null,
    expertDecisionComment = null,
    expertDecisionMachineLearning = null,
    expertDecisionProperSize = null,
    expertDecisionType = null,
  )

fun MarkModel.toExpertRoiEntity(
  researchId: Int,
  sameRoi: ExpertRoiEntity
): ExpertRoiEntity =
  ExpertRoiEntity(
    id = id,
    researchId = researchId,
    acquisitionNumber = sameRoi.acquisitionNumber,
    dcmFilename = sameRoi.dcmFilename,
    instanceNumber = sameRoi.instanceNumber,
    seriesNumber = sameRoi.seriesNumber,
    sopInstanceUid = sameRoi.sopInstanceUid,
    anatomicalLocation = sameRoi.anatomicalLocation,
    confidence = 1.0,
    roiFilename = sameRoi.roiFilename,
    roiShape = sameRoi.roiShape,
    roiType = type!!.typeId,
    roiTypeIndex = 1,
    taggerId = "",
    xCenter = markData.x,
    xSize = markData.sizeHorizontal,
    yCenter = markData.y,
    ySize = markData.sizeVertical,
    text = "",
  )