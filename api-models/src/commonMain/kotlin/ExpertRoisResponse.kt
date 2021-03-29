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
)