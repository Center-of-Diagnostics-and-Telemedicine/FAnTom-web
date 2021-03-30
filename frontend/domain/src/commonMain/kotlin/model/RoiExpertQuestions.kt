package model

data class RoiExpertQuestionsModel(
  val roiModel: ExpertRoiEntity,
  val expertQuestions: List<ExpertQuestion<*>>
)

fun RoiExpertQuestionsModel.toMarkModel(): MarkModel {

  return MarkModel(
    id = roiModel.id,
    markData = MarkData(
      x = roiModel.xCenter,
      y = roiModel.yCenter,
      z = -1.0,
      radiusHorizontal = roiModel.xSize / 2,
      radiusVertical = roiModel.ySize / 2,
      sizeHorizontal = roiModel.xSize,
      sizeVertical = roiModel.ySize,
      cutType = roiModel.cutType,
      shapeType = SHAPE_TYPE_RECTANGLE
    ),
    type = MarkTypeModel(
      typeId = roiModel.roiType,
      en = roiModel.roiType,
      ru = roiModel.roiType,
      color = defaultMarkColor
    ),
    comment = ""
  )
}

fun buildRoisToExpertMarks(
  rois: List<ExpertRoiEntity>,
  expertMarks: List<ExpertMarkEntity>
): List<RoiExpertQuestionsModel> {
  return rois.map { roi ->
    val existingExpertMark = expertMarks.firstOrNull { it.roiId == roi.id }
    if (existingExpertMark == null) {
      RoiExpertQuestionsModel(
        roiModel = roi,
        expertQuestions = expertQuestionsList
      )
    } else {
      RoiExpertQuestionsModel(
        roiModel = roi,
        expertQuestions = existingExpertMark.toExpertQuestionsList()
      )
    }
  }
}

