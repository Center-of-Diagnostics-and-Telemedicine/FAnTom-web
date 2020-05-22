package model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorModel(val error: Int, val message: String = "")

@Serializable
data class AuthorizationModel(val token: String)

@Serializable
data class AuthorizationResponse(
  val response: AuthorizationModel? = null,
  val error: ErrorModel? = null
)
