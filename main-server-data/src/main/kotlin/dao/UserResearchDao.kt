package dao

import UserResearchVos
import model.UserResearchModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import toUserResearch

class UserResearchDao() : UserResearchDaoFacade {

  override suspend fun getUserResearch(userId: Int, researchId: Int): UserResearchModel? {
    return transaction {
      UserResearchVos
        .select { UserResearchVos.userId eq userId and (UserResearchVos.researchId eq researchId) }
        .firstOrNull()
        ?.toUserResearch()
    }
  }

  override suspend fun getResearchesForUser(userId: Int): List<UserResearchModel> {
    return transaction {
      UserResearchVos
        .select { UserResearchVos.userId eq userId }
        .toList()
        .map { it.toUserResearch() }
    }
  }

  override suspend fun getUsersForResearch(researchId: Int): List<UserResearchModel> {
    return transaction {
      UserResearchVos
        .select { UserResearchVos.researchId eq researchId }
        .toList()
        .map { it.toUserResearch() }
    }
  }

  override suspend fun createUserResearch(userResearchModel: UserResearchModel) {
    return transaction {
      UserResearchVos.insert {
        it[userId] = userResearchModel.userId
        it[researchId] = userResearchModel.researchId
        it[seen] = if (userResearchModel.seen) 1 else 0
        it[done] = if (userResearchModel.done) 1 else 0
      }
    }
  }

  override suspend fun updateUserResearch(userResearchModel: UserResearchModel) {
    return transaction {
      UserResearchVos.update(where = { UserResearchVos.userId eq userResearchModel.userId and (UserResearchVos.researchId eq userResearchModel.researchId) }) {
        it[seen] = if (userResearchModel.seen) 1 else 0
        it[done] = if (userResearchModel.done) 1 else 0
      }
    }
  }

  override suspend fun deleteUserResearch(userId: Int, researchId: Int) {
    return transaction {
      UserResearchVos
        .deleteWhere { UserResearchVos.researchId eq researchId and (UserResearchVos.userId eq userId) }
    }
  }

  override suspend fun markSeen(userId: Int, researchId: Int) {
    return transaction {
      UserResearchVos.update(where = { UserResearchVos.userId eq userId and (UserResearchVos.researchId eq researchId) }) {
        it[seen] = 1
      }
    }
  }
}