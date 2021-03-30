package useCase

import model.*
import repository.repository.ExpertMarksRepository

suspend fun createMarks(
  usersToNodules: List<Map<UserModel, NoduleModel?>>,
  users: List<UserModel>,
  research: ResearchModel,
  repository: ExpertMarksRepository
) {
//  usersToNodules.forEach { usersToNodulesMap ->
//    usersToNodulesMap.forEach { user, nodule ->
//      if (nodule != null) {
//        createMark(
//          repository = repository,
//          nodule = nodule,
//          user = user,
//          research = research
//        )
//      }
//    }
//  }
}

private suspend fun createMark(
  repository: ExpertMarksRepository,
  nodule: NoduleModel,
  user: UserModel,
  research: ResearchModel
) {
  val expertDecisionModel = nodule.expertDecision?.firstOrNull()
  repository.create(
    mark = ExpertMarkModel(
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