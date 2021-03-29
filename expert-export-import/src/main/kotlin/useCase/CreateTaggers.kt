package useCase

import model.*
import repository.UserRepository

suspend fun createTaggers(
  taggersIds: Map<String, TaggerModel>,
  userRepository: UserRepository
): List<UserModel> {
//  val users = mutableListOf<UserModel>()
//
//  taggersIds.forEach { name, model ->
//    val password = hash(defaultUserPassword)
//
//    val existingUser = userRepository.getUser(name, password)
//    if (existingUser != null) {
//      users.add(existingUser)
//    } else {
//      userRepository.createUser(name, password, UserRole.TAGGER.value)
//      userRepository.getUser(name, password)?.let { users.add(it) }
//    }
//  }
//
//  return users
  return listOf()
}