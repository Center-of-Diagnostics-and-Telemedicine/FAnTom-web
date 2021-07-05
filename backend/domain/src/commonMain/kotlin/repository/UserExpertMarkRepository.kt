package repository

import model.UserExpertMarkModel

interface UserExpertMarkRepository {
  suspend fun getUserExpertMarks(userId: Int, researchId: Int): List<UserExpertMarkModel>
  suspend fun createUserExpertMark(userExpertMarkModel: UserExpertMarkModel)
  suspend fun deleteUserExpertMark(userId: Int, researchId: Int, markId: Int)
}