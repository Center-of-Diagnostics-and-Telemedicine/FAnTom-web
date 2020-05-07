package dao

import UserResearchVos
import model.UserResearchModel
import org.jetbrains.exposed.sql.*
import toUserResearch
import util.database

interface UserResearchDaoFacade {
  suspend fun getUserResearch(userId: Int, researchId: Int): UserResearchModel?
  suspend fun getResearchesForUser(userId: Int): List<UserResearchModel>
  suspend fun getUsersForResearch(researchId: Int): List<UserResearchModel>
  suspend fun createUserResearch(userResearchModel: UserResearchModel)
  suspend fun updateUserResearch(userResearchModel: UserResearchModel)
  suspend fun deleteUserResearch(userId: Int, researchId: Int)
}

class UserResearchDao() : UserResearchDaoFacade {

  override suspend fun getUserResearch(userId: Int, researchId: Int): UserResearchModel? {
    return database {
      UserResearchVos
        .select { UserResearchVos.userId eq userId and (UserResearchVos.researchId eq researchId) }
        .firstOrNull()
        ?.toUserResearch()
    }
  }

  override suspend fun getResearchesForUser(userId: Int): List<UserResearchModel> {
    return database {
      UserResearchVos
        .select { UserResearchVos.userId eq userId }
        .map { it.toUserResearch() }
        .toList()
    }
  }

  override suspend fun getUsersForResearch(researchId: Int): List<UserResearchModel> {
    return database {
      UserResearchVos
        .select { UserResearchVos.researchId eq researchId }
        .map { it.toUserResearch() }
        .toList()
    }
  }

  override suspend fun createUserResearch(userResearchModel: UserResearchModel) {
    return database {
      UserResearchVos.insert {
        it[userId] = userResearchModel.userId
        it[researchId] = userResearchModel.researchId
        it[seen] = if (userResearchModel.seen) 1 else 0
        it[done] = if (userResearchModel.done) 1 else 0
      }
    }
  }

  override suspend fun updateUserResearch(userResearchModel: UserResearchModel) {
    return database {
      UserResearchVos.update(where = { UserResearchVos.userId eq userResearchModel.userId and (UserResearchVos.researchId eq userResearchModel.researchId) }) {
        it[seen] = if (userResearchModel.seen) 1 else 0
        it[done] = if (userResearchModel.done) 1 else 0
      }
    }
  }

  override suspend fun deleteUserResearch(userId: Int, researchId: Int) {
    return database {
      UserResearchVos
        .deleteWhere { UserResearchVos.researchId eq researchId and (UserResearchVos.userId eq userId) }
    }
  }
}