package model

data class RoiExpertQuestionsModel(
  val roiModel: ExpertRoiEntity,
  val expertQuestions: List<ExpertQuestion<*>>
)

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

