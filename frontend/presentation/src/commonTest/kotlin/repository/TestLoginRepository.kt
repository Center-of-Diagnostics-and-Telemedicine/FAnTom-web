package repository

import model.ResearchApiExceptions
import testLogin
import testPassword

class TestLoginRepository : LoginRepository {

  override suspend fun auth(login: String, password: String) {
    if (login == testLogin && password == testPassword) {
      return
    } else {
      throw ResearchApiExceptions.InvalidAuthCredentials
    }
  }

  override suspend fun tryToAuth(login: String, password: String) {
    TODO("Not yet implemented")
  }

}
