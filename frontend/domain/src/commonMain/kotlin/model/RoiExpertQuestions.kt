package model

data class RoiExpertQuestionsModel(
  val roiModel: ExpertRoiEntity,
  val expertQuestions: List<ExpertQuestion<*>>,
  val color: String
) {
  var selected: Boolean = false
}

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
      color = color
    ),
    comment = ""
  ).also {
    it.selected = selected
    it.editable = false
  }
}

fun buildRoisToExpertMarks(
  rois: List<ExpertRoiEntity>,
  expertMarks: List<ExpertMarkEntity>,
  markTypes: Map<String, MarkTypeEntity>
): List<RoiExpertQuestionsModel> {
  return rois.map { roi ->
    val existingExpertMark = expertMarks.firstOrNull { it.roiId == roi.id }
    if (existingExpertMark == null) {
      RoiExpertQuestionsModel(
        roiModel = roi,
        expertQuestions = expertQuestionsList,
        color = markTypes[roi.roiType]?.CLR ?: defaultMarkColor
      )
    } else {
      RoiExpertQuestionsModel(
        roiModel = roi,
        expertQuestions = existingExpertMark.toExpertQuestionsList(),
        color = markTypes[roi.roiType]?.CLR ?: defaultMarkColor
      )
    }
  }
}

