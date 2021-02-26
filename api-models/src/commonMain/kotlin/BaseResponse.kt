package model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorModel(val error: Int, val message: String = "")

@Serializable
data class BaseResponse(
  val response: OK? = null,
  val error: ErrorModel? = null
)
