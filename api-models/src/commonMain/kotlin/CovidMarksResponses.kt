package model

import kotlinx.serialization.Serializable

@Serializable
data class CovidMarksResponse(
  val response: CovidMarkEntity? = null,
  val error: ErrorModel? = null
)