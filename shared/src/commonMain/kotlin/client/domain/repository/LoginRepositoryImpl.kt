package client.domain.repository

import client.datasource.local.LocalDataSource
import client.datasource.remote.LoginRemote
import client.debugLog

class LoginRepositoryImpl(
  private val local: LocalDataSource,
  private val remote: LoginRemote
) : LoginRepository {

  override suspend fun auth(login: String, password: String) {
    debugLog("repository:    name: $login password:$password")
    val token = remote.auth(login, password)
    local.saveToken(token = token)
  }

  override suspend fun tryToAuth() {
    val token = local.getToken()
    if (token.isNotEmpty()) {
      remote.tryToAuth()
    } else {
      throw Exception()
    }
  }

}