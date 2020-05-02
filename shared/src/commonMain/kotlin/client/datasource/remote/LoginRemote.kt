package client.datasource.remote

interface LoginRemote {
  suspend fun auth(login: String, password: String): String
  suspend fun tryToAuth(): String
}