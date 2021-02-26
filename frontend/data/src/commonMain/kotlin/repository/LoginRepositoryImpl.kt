package repository

import model.*
import model.ResearchApiExceptions.*

class LoginRepositoryImpl(
  val local: LoginLocal,
  val remote: LoginRemote
) : LoginRepository {

  override suspend fun auth(login: String, password: String) {
    val response = remote.auth(AuthorizationRequest(login, password))
    return when {
      response.response != null -> local.saveToken(token = response.response!!.token)
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw AuthFailedException
    }
  }

  override suspend fun tryToAuth(login: String, password: String) {
    remote.tryToAuth()
  }

  private fun <T : Any> handleErrorResponse(errorModel: ErrorModel): T {
    when (errorModel.error) {
      ErrorStringCode.AUTH_FAILED.value -> throw AuthFailedException
      ErrorStringCode.INVALID_AUTH_CREDENTIALS.value -> throw InvalidAuthCredentials
      else -> throw Exception(BASE_ERROR)
    }
  }

}
