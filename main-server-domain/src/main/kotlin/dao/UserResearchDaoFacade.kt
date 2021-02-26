package dao

import model.UserResearchModel

interface UserResearchDaoFacade {
  suspend fun getUserResearch(userId: Int, researchId: Int): UserResearchModel?
  suspend fun getResearchesForUser(userId: Int): List<UserResearchModel>
  suspend fun getUsersForResearch(researchId: Int): List<UserResearchModel>
  suspend fun createUserResearch(userResearchModel: UserResearchModel)
  suspend fun updateUserResearch(userResearchModel: UserResearchModel)
  suspend fun deleteUserResearch(userId: Int, researchId: Int)
  suspend fun markSeen(userId: Int, researchId: Int)
}