package model

data class ExpertMarkModel(
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
  val confirmed: Boolean?
)

fun ExpertMarkModel.toExpertMarkEntity(): ExpertMarkEntity =
  ExpertMarkEntity(
    id = id,
    roiId = roiId,
    xCenter = xCenter,
    yCenter = yCenter,
    xSize = xSize,
    ySize = ySize,
    researchId = researchId,
    acquisitionNumber = acquisitionNumber,
    dcmFilename = dcmFilename,
    instanceNumber = instanceNumber,
    seriesNumber = seriesNumber,
    sopInstanceUid = sopInstanceUid,
    anatomicalLocation = anatomicalLocation,
    confidence = confidence,
    roiFilename = roiFilename,
    roiShape = roiShape,
    roiType = roiType,
    roiTypeIndex = roiTypeIndex,
    taggerId = taggerId,
    text = text,
    confirmed = confirmed
  )

fun ExpertMarkEntity.toExpertMarkModel(): ExpertMarkModel =
  ExpertMarkModel(
    id = id,
    roiId = roiId,
    xCenter = xCenter,
    yCenter = yCenter,
    xSize = xSize,
    ySize = ySize,
    researchId = researchId,
    acquisitionNumber = acquisitionNumber,
    dcmFilename = dcmFilename,
    instanceNumber = instanceNumber,
    seriesNumber = seriesNumber,
    sopInstanceUid = sopInstanceUid,
    anatomicalLocation = anatomicalLocation,
    confidence = confidence,
    roiFilename = roiFilename,
    roiShape = roiShape,
    roiType = roiType,
    roiTypeIndex = roiTypeIndex,
    taggerId = taggerId,
    text = text,
    confirmed = confirmed
  )