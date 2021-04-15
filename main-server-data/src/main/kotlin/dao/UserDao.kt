package dao

import UserVos
import model.UserModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import toUser

class UserDao() : UserDaoFacade {

  override suspend fun getUserByCredentials(login: String, hashedPassword: String): UserModel? {
    return transaction {
      UserVos
        .select { UserVos.name eq login and (UserVos.password eq hashedPassword) }
        .firstOrNull()
        ?.toUser()
    }
  }

  override suspend fun getUserByLogin(login: String): UserModel? {
    return transaction {
      UserVos
        .select { UserVos.name eq login }
        .firstOrNull()
        ?.toUser()
    }
  }

  override suspend fun getUserById(userId: Int): UserModel? {
    return transaction {
      UserVos
        .select { UserVos.id eq userId }
        .firstOrNull()
        ?.toUser()
    }
  }

  override suspend fun createUser(login: String, hashedPassword: String, userRole: Int) {
    return transaction {
      UserVos.insert {
        it[name] = login
        it[password] = hashedPassword
        it[role] = userRole
      }
    }
  }

  override suspend fun deleteUser(userId: Int) {
    transaction {
      UserVos.deleteWhere { UserVos.id eq userId }
    }
  }

  override suspend fun updateUser(
    userId: Int,
    login: String,
    hashedPassword: String,
    userRole: Int
  ) {
    transaction {
      UserVos.update(where = { UserVos.id eq userId }) {
        it[name] = login
        it[password] = hashedPassword
        it[role] = userRole
      }
    }
  }

  override suspend fun getAll(): List<UserModel> {
    return transaction {
      UserVos
        .selectAll()
        .map(ResultRow::toUser)
    }
  }

}