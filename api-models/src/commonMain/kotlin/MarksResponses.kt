package model

import kotlinx.serialization.Serializable

@Serializable
data class MarksModel(val list: List<MarkEntity>)

@Serializable
data class MarkDomainModel(val mark: MarkEntity)

@Serializable
data class MarksResponseNew(
  val response: MarksModel? = null,
  val error: ErrorModel? = null
)

@Serializable
data class MarkResponse(
  val response: MarkDomainModel? = null,
  val error: ErrorModel? = null
)
