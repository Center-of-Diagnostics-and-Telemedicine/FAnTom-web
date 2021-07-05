package model

import kotlinx.serialization.Serializable

@Serializable
data class ResearchInitResponse(
  val response: ResearchInitModel? = null,
  val error: ErrorModel? = null,
)

@Serializable
data class ResearchInitModel(
  val CT: ModalityModel? = null,
  val MG: ModalityModel? = null,
  val DX: ModalityModel? = null,
  val dictionary: Map<String, MarkTypeEntity>? = null
)

@Serializable
data class ModalityModel(
  val planes: Map<String, PlaneModel>,
  val reversed: Boolean
)