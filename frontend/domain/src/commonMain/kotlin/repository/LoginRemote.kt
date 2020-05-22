package repository

import model.AuthorizationRequest
import model.AuthorizationResponse

interface LoginRemote {
  suspend fun auth(request: AuthorizationRequest): AuthorizationResponse
  suspend fun tryToAuth(): AuthorizationResponse
}