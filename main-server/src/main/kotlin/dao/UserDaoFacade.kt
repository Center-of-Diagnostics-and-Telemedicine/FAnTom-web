package dao

import UserVos
import model.UserModel
import org.jetbrains.exposed.sql.*
import toUser
import util.database

interface UserDaoFacade {
  suspend fun getUserByCredentials(login: String, hashedPassword: String): UserModel?
  suspend fun getUserByLogin(login: String): UserModel?
  suspend fun getUserById(userId: Int): UserModel?
  suspend fun createUser(login: String, hashedPassword: String, userRole: Int)
  suspend fun deleteUser(userId: Int)
  suspend fun updateUser(userId: Int, login: String, hashedPassword: String, userRole: Int)
}

class UserDao() : UserDaoFacade {

  override suspend fun getUserByCredentials(login: String, hashedPassword: String): UserModel? {
    return database {
      UserVos
        .select { UserVos.name eq login and (UserVos.password eq hashedPassword) }
        .firstOrNull()
        ?.toUser()
    }
  }

  override suspend fun getUserByLogin(login: String): UserModel? {
    return database {
      UserVos
        .select { UserVos.name eq login }
        .firstOrNull()
        ?.toUser()
    }
  }

  override suspend fun getUserById(userId: Int): UserModel? {
    return database {
      UserVos
        .select { UserVos.id eq userId }
        .firstOrNull()
        ?.toUser()
    }
  }

  override suspend fun createUser(login: String, hashedPassword: String, userRole: Int) {
    return database {
      UserVos.insert {
        it[name] = login
        it[password] = hashedPassword
        it[role] = userRole
      }
    }
  }

  override suspend fun deleteUser(userId: Int) {
    database {
      UserVos.deleteWhere { UserVos.id eq userId }
    }
  }

  override suspend fun updateUser(
    userId: Int,
    login: String,
    hashedPassword: String,
    userRole: Int
  ) {
    database {
      UserVos.update(where = { UserVos.id eq userId }) {
        it[name] = login
        it[password] = hashedPassword
        it[role] = userRole
      }
    }
  }

}