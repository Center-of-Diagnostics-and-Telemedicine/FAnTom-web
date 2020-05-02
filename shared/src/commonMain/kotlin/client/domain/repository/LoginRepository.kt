package client.domain.repository

interface LoginRepository : Repository {

  suspend fun auth(login: String, password: String)
  suspend fun tryToAuth()
}