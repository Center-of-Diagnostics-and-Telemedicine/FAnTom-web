package client.domain.repository

import client.ResearchApiExceptions
import client.datasource.local.LocalDataSource
import client.datasource.remote.LoginRemote
import client.debugLog
import model.*

interface LoginRepository : Repository {

  suspend fun auth(login: String, password: String)
  suspend fun tryToAuth()
}

class LoginRepositoryImpl(
  private val local: LocalDataSource,
  private val remote: LoginRemote
) : LoginRepository {

  override suspend fun auth(login: String, password: String) {
    debugLog("repository:    name: $login password:$password")
    return when (val response = remote.auth(login, password)) {
      is ApiResponse.AuthorizationResponse -> local.saveToken(token = response.token)
      is ApiResponse.ErrorResponse -> handleErrorResponse(response)
      else -> throw ResearchApiExceptions.AuthFailedException
    }

  }

  override suspend fun tryToAuth() {
    val token = local.getToken()
    if (token.isNotEmpty()) {
      remote.tryToAuth()
    } else {
      throw Exception()
    }
  }

  private fun <T : Any> handleErrorResponse(response: ApiResponse.ErrorResponse): T {
    when (response.errorCode) {
      ErrorStringCode.AUTH_FAILED.value -> throw ResearchApiExceptions.AuthFailedException
      ErrorStringCode.INVALID_AUTH_CREDENTIALS.value -> throw ResearchApiExceptions.InvalidAuthCredentials
      else -> throw Exception(BASE_ERROR)
    }
  }

}