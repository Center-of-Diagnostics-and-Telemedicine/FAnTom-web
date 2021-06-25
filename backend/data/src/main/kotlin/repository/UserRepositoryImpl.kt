package repository

import repository.dao.UserDaoFacade
import model.UserModel
import model.UserRole
import repository.repository.UserRepository

class UserRepositoryImpl(private val userDaoFacade: UserDaoFacade) : UserRepository {

  override suspend fun getUser(login: String, hashedPassword: String): UserModel? {
    return userDaoFacade.getUserByCredentials(login, hashedPassword)
  }

  override suspend fun createUser(login: String, hashedPassword: String, role: Int) {
    val existingUser = userDaoFacade.getUserByLogin(login)
    if (existingUser != null) {
      throw IllegalStateException("user exists")
    }
    userDaoFacade.createUser(login, hashedPassword, role)
  }

  override suspend fun deleteUser(userId: Int) {
    checkUserExistence(userId)
    userDaoFacade.deleteUser(userId)
  }

  override suspend fun updateUser(
    userId: Int,
    login: String,
    hashedPassword: String,
    role: UserRole
  ) {
    checkUserExistence(userId)
    userDaoFacade.updateUser(userId, login, hashedPassword, role.value)
  }

  override suspend fun getAll(): List<UserModel> {
    return userDaoFacade.getAll()
  }

  private suspend fun checkUserExistence(userId: Int) =
    userDaoFacade.getUserById(userId)
      ?: throw IllegalStateException("user not found")

}