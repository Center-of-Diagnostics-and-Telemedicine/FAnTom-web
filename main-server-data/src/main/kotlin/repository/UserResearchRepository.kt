package repository

import dao.UserResearchDaoFacade
import model.UserResearchModel

class UserResearchRepositoryImpl(private val userResearchDaoFacade: UserResearchDaoFacade) :
  UserResearchRepository {

  override suspend fun getAll(): List<UserResearchModel> {
    return userResearchDaoFacade.getAll()
  }

  override suspend fun getResearchesForUser(userId: Int): List<UserResearchModel> {
    return userResearchDaoFacade.getResearchesForUser(userId)
  }

  override suspend fun getUsersForResearch(researchId: Int): List<UserResearchModel> {
    return userResearchDaoFacade.getUsersForResearch(researchId)
  }

  override suspend fun createUserResearch(userResearchModel: UserResearchModel) {
    val existingUserResearch = userResearchDaoFacade.getUserResearch(
      userId = userResearchModel.userId,
      researchId = userResearchModel.researchId
    )
    if (existingUserResearch != null) {
      throw IllegalStateException("user exists")
    }
    userResearchDaoFacade.createUserResearch(userResearchModel)
  }

  override suspend fun updateUserResearch(userResearchModel: UserResearchModel) {
    checkUserResearchExistence(
      userId = userResearchModel.userId,
      researchId = userResearchModel.researchId
    )
    userResearchDaoFacade.updateUserResearch(userResearchModel)
  }

  override suspend fun deleteUserResearch(userId: Int, researchId: Int) {
    checkUserResearchExistence(userId = userId, researchId = researchId)
    userResearchDaoFacade.deleteUserResearch(userId, researchId)

  }

  override suspend fun markSeen(userId: Int, researchId: Int) {
    userResearchDaoFacade.markSeen(userId, researchId)
  }

  private suspend fun checkUserResearchExistence(userId: Int, researchId: Int) =
    userResearchDaoFacade.getUserResearch(userId = userId, researchId = researchId)
      ?: throw IllegalStateException("user not found")
}