package repository.dao

import model.UserExpertMarkModel

interface UserExpertMarkDaoFacade {
  suspend fun getUserExpertMarks(userId: Int, researchId: Int): List<UserExpertMarkModel>
  suspend fun getUserExpertMark(userId: Int, researchId: Int, markId: Int): UserExpertMarkModel?
  suspend fun createUserExpertMark(userExpertMarkModel: UserExpertMarkModel)
  suspend fun deleteUserExpertMark(userId: Int, researchId: Int, markId: Int)
}