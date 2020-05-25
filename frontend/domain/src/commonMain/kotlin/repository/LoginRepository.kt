package repository

interface LoginRepository {

  val local: LoginLocal
  val remote: LoginRemote

  suspend fun auth(login: String, password: String)
  suspend fun tryToAuth(login: String, password: String)
}
