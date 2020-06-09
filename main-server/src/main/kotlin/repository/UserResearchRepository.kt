package repository

import dao.UserResearchDaoFacade
import model.UserResearchModel

interface UserResearchRepository {
  suspend fun getResearchesForUser(userId: Int): List<UserResearchModel>
  suspend fun getUsersForResearch(researchId: Int): List<UserResearchModel>
  suspend fun createUserResearch(userResearchModel: UserResearchModel)
  suspend fun updateUserResearch(userResearchModel: UserResearchModel)
  suspend fun deleteUserResearch(userId: Int, researchId: Int)
  suspend fun markSeen(userId: Int, researchId: Int)
}

class UserResearchRepositoryImpl(private val userResearchDaoFacade: UserResearchDaoFacade) :
  UserResearchRepository {

  override suspend fun getResearchesForUser(userId: Int): List<UserResearchModel> {
    return userResearchDaoFacade.getResearchesForUser(userId)
  }

  override suspend fun getUsersForResearch(researchId: Int): List<UserResearchModel> {
    return userResearchDaoFacade.getUsersForResearch(researchId)
  }

  override suspend fun createUserResearch(userResearchModel: UserResearchModel) {
    checkUserResearchExistence(
      userId = userResearchModel.userId,
      researchId = userResearchModel.researchId
    )
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
