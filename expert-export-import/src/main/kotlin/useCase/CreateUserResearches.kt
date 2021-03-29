package useCase

import model.ResearchModel
import model.UserModel
import model.UserResearchModel
import repository.UserResearchRepository

suspend fun createUsersResearchRelationModel(
  userModels: List<UserModel>,
  research: ResearchModel,
  repository: UserResearchRepository
): List<UserResearchModel> {
  return userModels
    .map { user ->
      repository.createUserResearch(userResearchModel(user, research))
      repository.getUsersForResearch(user.id)
    }
    .flatten()
    .distinct()
}

private fun userResearchModel(
  user: UserModel,
  research: ResearchModel
) = UserResearchModel(
  userId = user.id,
  researchId = research.id,
  seen = true,
  done = true
)