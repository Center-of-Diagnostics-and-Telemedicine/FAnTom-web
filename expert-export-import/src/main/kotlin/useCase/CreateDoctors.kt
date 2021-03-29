package useCase

import model.*
import repository.UserRepository

suspend fun createDoctors(
  doctorsIds: List<String>,
  userRepository: UserRepository
): List<UserModel> {
  val users = mutableListOf<UserModel>()

  doctorsIds.forEach { id ->
    val login = DEFAULT_USER_NAME + "_" + id
    val password = hash(defaultUserPassword)

    val existingUser = userRepository.getUser(login, password)
    if (existingUser != null) {
      users.add(existingUser)
    } else {
      userRepository.createUser(login, password, UserRole.TAGGER.value)
      userRepository.getUser(login, password)?.let { users.add(it) }
    }
  }

  return users
}