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
    val response = remote.auth(login, password)
    return when {
      response.response != null -> local.saveToken(token = response.response!!.token)
      response.error != null -> handleErrorResponse(response.error!!)
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

  private fun <T : Any> handleErrorResponse(errorModel: ErrorModel): T {
    when (errorModel.error) {
      ErrorStringCode.AUTH_FAILED.value -> throw ResearchApiExceptions.AuthFailedException
      ErrorStringCode.INVALID_AUTH_CREDENTIALS.value -> throw ResearchApiExceptions.InvalidAuthCredentials
      else -> throw Exception(BASE_ERROR)
    }
  }

}