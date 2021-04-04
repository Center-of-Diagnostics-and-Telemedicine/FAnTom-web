package model

import kotlinx.serialization.Serializable

@Serializable
data class ExpertMarkEntity(
  val id: Int,
  val roiId: Int?,
  val xCenter: Double,
  val yCenter: Double,
  val xSize: Double,
  val ySize: Double,
  val researchId: Int,
  val acquisitionNumber: String,
  val dcmFilename: String,
  val instanceNumber: Int,
  val seriesNumber: Int,
  val sopInstanceUid: String,
  val anatomicalLocation: String,
  val confidence: Double,
  val roiFilename: String,
  val roiShape: String,
  val roiType: String,
  val roiTypeIndex: Int,
  val taggerId: String,
  val text: String,
  val confirmed: Boolean? = null
){
  val cutType: Int
    get() = when (instanceNumber) {
      1 -> SLICE_TYPE_CT_0
      2 -> SLICE_TYPE_CT_1
      3 -> SLICE_TYPE_CT_2
      else -> SLICE_TYPE_CT_0
    }

  val prettyPrint: String
    get() = "x:$xCenter,y:$yCenter,w:$xSize,h$ySize"
}

fun MarkModel.toEmptyExpertMarkEntity(saveMarkEntity: ExpertMarkEntity): ExpertMarkEntity =
  ExpertMarkEntity(
    id = -1,
    roiId = -1,
    researchId = saveMarkEntity.researchId,
    xCenter = markData.x,
    yCenter = markData.y,
    xSize = markData.sizeHorizontal,
    ySize = markData.sizeVertical,
    acquisitionNumber = saveMarkEntity.acquisitionNumber,
    dcmFilename = saveMarkEntity.dcmFilename,
    instanceNumber = saveMarkEntity.instanceNumber,
    seriesNumber = saveMarkEntity.seriesNumber,
    sopInstanceUid = saveMarkEntity.sopInstanceUid,
    anatomicalLocation = saveMarkEntity.anatomicalLocation,
    confidence = saveMarkEntity.confidence,
    roiFilename = saveMarkEntity.roiFilename,
    roiShape = saveMarkEntity.roiShape,
    roiType = type!!.en.replace("\\s".toRegex(), ""),
    roiTypeIndex = saveMarkEntity.roiTypeIndex,
    taggerId = saveMarkEntity.taggerId,
    confirmed = null,
    text = ""
  )