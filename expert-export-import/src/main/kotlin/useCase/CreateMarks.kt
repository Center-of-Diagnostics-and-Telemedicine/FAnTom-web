package useCase

import model.*
import repository.repository.ExportedMarksRepository

suspend fun createMarks(
  usersToNodules: List<Map<UserModel, NoduleModel?>>,
  users: List<UserModel>,
  research: ResearchModel,
  repository: ExportedMarksRepository
) {
  usersToNodules.forEach { usersToNodulesMap ->
    usersToNodulesMap.forEach { user, nodule ->
      if (nodule != null) {
        createMark(
          repository = repository,
          nodule = nodule,
          user = user,
          research = research
        )
      }
    }
  }
}

private suspend fun createMark(
  repository: ExportedMarksRepository,
  nodule: NoduleModel,
  user: UserModel,
  research: ResearchModel
) {
  val expertDecisionModel = nodule.expertDecision?.firstOrNull()
  repository.create(
    mark = ExportedMarkModel(
      id = -1,
      diameterMm = nodule.diameterMm,
      type = nodule.type,
      version = nodule.version,
      x = nodule.x,
      y = nodule.y,
      z = nodule.z,
      zType = nodule.zType,
      expertDecision = expertDecisionModel?.decision,
      expertDecisionId = expertDecisionModel?.id,
      expertDecisionComment = expertDecisionModel?.comment,
      expertDecisionMachineLearning = expertDecisionModel?.machineLearning,
      expertDecisionProperSize = expertDecisionModel?.properSize,
      expertDecisionType = expertDecisionModel?.type,
    ),
    userId = user.id,
    researchId = research.id
  )
}