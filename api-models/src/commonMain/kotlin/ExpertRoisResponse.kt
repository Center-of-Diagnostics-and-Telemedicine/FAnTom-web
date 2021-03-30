package model

import kotlinx.serialization.Serializable

@Serializable
data class ExpertRoisResponse(
  val response: List<ExpertRoiEntity>? = null,
  val error: ErrorModel? = null
)

@Serializable
data class ExpertRoiEntity(
  val id: Int,
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
  val xCenter: Double,
  val xSize: Double,
  val yCenter: Double,
  val ySize: Double,
  val text: String,
) {
  val cutType: Int
    get() = when (instanceNumber) {
      0 -> SLICE_TYPE_CT_0
      1 -> SLICE_TYPE_CT_1
      2 -> SLICE_TYPE_CT_2
      else -> SLICE_TYPE_CT_0
    }

  val prettyPrint: String
    get() = "x:$xCenter,y:$yCenter,w:$xSize,h$ySize"
}