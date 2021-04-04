package model

data class ExpertQuestionsModel(
  val expertMarkEntity: ExpertMarkEntity,
  val expertQuestions: List<ExpertQuestion<*>>,
  val color: String,
  val confirmed: Boolean? = null
) {
  var selected: Boolean = false
}

fun ExpertQuestionsModel.toMarkModel(): MarkModel {

  return MarkModel(
    id = expertMarkEntity.id,
    markData = MarkData(
      x = expertMarkEntity.xCenter,
      y = expertMarkEntity.yCenter,
      z = -1.0,
      radiusHorizontal = expertMarkEntity.xSize / 2,
      radiusVertical = expertMarkEntity.ySize / 2,
      sizeHorizontal = expertMarkEntity.xSize,
      sizeVertical = expertMarkEntity.ySize,
      cutType = expertMarkEntity.cutType,
      shapeType = SHAPE_TYPE_RECTANGLE
    ),
    type = MarkTypeModel(
      typeId = expertMarkEntity.roiType,
      en = expertMarkEntity.roiType,
      ru = expertMarkEntity.roiType,
      color = color
    ),
    comment = ""
  ).also {
    it.selected = selected
    it.editable = false
  }
}

fun buildMarksQuestions(
  expertMarks: List<ExpertMarkEntity>,
  markTypes: Map<String, MarkTypeEntity>
): List<ExpertQuestionsModel> {
  return expertMarks.map {
    if (it.confirmed == true) {
      ExpertQuestionsModel(
        expertMarkEntity = it,
        expertQuestions = expertQuestionsList,
        color = markTypes[it.roiType]?.CLR ?: defaultMarkColor,
      )
    } else {
      ExpertQuestionsModel(
        expertMarkEntity = it,
        confirmed = it.confirmed,
        expertQuestions = it.toExpertQuestionsList(),
        color = markTypes[it.roiType]?.CLR ?: defaultMarkColor
      )
    }
  }
}

