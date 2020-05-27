import model.ResearchApiExceptions
import repository.LoginRepository

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
