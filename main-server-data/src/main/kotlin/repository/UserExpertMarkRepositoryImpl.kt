package repository

import model.UserExpertMarkModel
import dao.UserExpertMarkDaoFacade

class UserExpertMarkRepositoryImpl(private val daoFacade: UserExpertMarkDaoFacade) :
  UserExpertMarkRepository {

  override suspend fun getUserExpertMarks(userId: Int, researchId: Int): List<UserExpertMarkModel> {
    return daoFacade.getUserExpertMarks(userId, researchId)
  }

  override suspend fun createUserExpertMark(userExpertMarkModel: UserExpertMarkModel) {
    val existingUserExpertMark = daoFacade.getUserExpertMark(
      userId = userExpertMarkModel.userId,
      researchId = userExpertMarkModel.researchId,
      markId = userExpertMarkModel.markId
    )
    if (existingUserExpertMark != null) {
      throw IllegalStateException("user exists")
    }
    daoFacade.createUserExpertMark(userExpertMarkModel)
  }

  override suspend fun deleteUserExpertMark(userId: Int, researchId: Int, markId: Int) {
    checkUserExpertMarkExistence(userId = userId, researchId = researchId, markId = markId)
    daoFacade.deleteUserExpertMark(userId, researchId, markId)
  }

  private suspend fun checkUserExpertMarkExistence(userId: Int, researchId: Int, markId: Int) =
    daoFacade.getUserExpertMark(userId = userId, researchId = researchId, markId = markId)
      ?: throw IllegalStateException("user not found")
}