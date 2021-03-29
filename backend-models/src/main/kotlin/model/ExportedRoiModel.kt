package model

data class ExportedRoiModel(
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