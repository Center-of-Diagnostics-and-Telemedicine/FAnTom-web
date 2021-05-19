package dao

import UserExpertMarkVos
import model.UserExpertMarkModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import toUserExpertMark

class UserExpertMarkDao() : UserExpertMarkDaoFacade {

  override suspend fun getUserExpertMarks(userId: Int, researchId: Int): List<UserExpertMarkModel> {
    return transaction {
      UserExpertMarkVos
        .select { UserExpertMarkVos.userId eq userId and (UserExpertMarkVos.researchId eq researchId) }
        .mapNotNull(ResultRow::toUserExpertMark)
    }
  }

  override suspend fun getUserExpertMark(
    userId: Int,
    researchId: Int,
    markId: Int
  ): UserExpertMarkModel? {
    return transaction {
      UserExpertMarkVos
        .select { UserExpertMarkVos.userId eq userId and (UserExpertMarkVos.researchId eq researchId) and (UserExpertMarkVos.expertMarkId eq markId) }
        .firstOrNull()
        ?.toUserExpertMark()
    }
  }

  override suspend fun createUserExpertMark(userExpertMarkModel: UserExpertMarkModel) {
    return transaction {
      UserExpertMarkVos.insert {
        it[userId] = userExpertMarkModel.userId
        it[researchId] = userExpertMarkModel.researchId
        it[expertMarkId] = userExpertMarkModel.markId
      }
    }
  }

  override suspend fun deleteUserExpertMark(userId: Int, researchId: Int, markId: Int) {
    return transaction {
      UserExpertMarkVos
        .deleteWhere { UserExpertMarkVos.researchId eq researchId and (UserExpertMarkVos.userId eq userId) }
    }
  }
}