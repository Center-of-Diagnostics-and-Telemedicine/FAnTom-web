package repository.dao

import model.UserModel

interface UserDaoFacade {
  suspend fun getUserByCredentials(login: String, hashedPassword: String): UserModel?
  suspend fun getUserByLogin(login: String): UserModel?
  suspend fun getUserById(userId: Int): UserModel?
  suspend fun createUser(login: String, hashedPassword: String, userRole: Int)
  suspend fun deleteUser(userId: Int)
  suspend fun updateUser(userId: Int, login: String, hashedPassword: String, userRole: Int)
  suspend fun getAll(): List<UserModel>
}

