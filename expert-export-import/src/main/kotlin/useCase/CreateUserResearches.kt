package useCase

import model.ResearchModel
import model.UserModel
import model.UserResearchModel
import repository.UserResearchRepository

fun createUserResearches(
  userModels: List<UserModel>,
  researchModels: List<ResearchModel>,
  repository: UserResearchRepository
): List<UserResearchModel> {
  val userResearches = mutableListOf<UserResearchModel>()

  userModels.forEach { user ->
    researchModels.forEach { research ->
      repository.createUserResearch(userResearchModel(user, research))
      userResearches.add(repository.getResearchesForUser(user.id))
    }
  }

  return userResearches
}

private suspend fun userResearchModel(
  user: UserModel,
  research: ResearchModel
) = UserResearchModel(
  userId = user.id,
  researchId = research.id,
  seen = true,
  done = true
)