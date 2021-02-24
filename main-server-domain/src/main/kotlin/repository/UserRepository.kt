package repository

import model.UserModel
import model.UserRole

interface UserRepository {
  suspend fun getUser(login: String, hashedPassword: String): UserModel?
  suspend fun createUser(login: String, hashedPassword: String, role: Int)
  suspend fun deleteUser(userId: Int)
  suspend fun updateUser(userId: Int, login: String, hashedPassword: String, role: UserRole)
}

