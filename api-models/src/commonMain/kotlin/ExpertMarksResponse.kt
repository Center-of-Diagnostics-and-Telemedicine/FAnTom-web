package model

import kotlinx.serialization.Serializable

@Serializable
data class ExpertMarksResponse(
  val response: List<ExpertMarkEntity>? = null,
  val error: ErrorModel? = null
)