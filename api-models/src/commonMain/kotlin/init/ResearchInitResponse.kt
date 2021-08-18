package model.init

import kotlinx.serialization.Serializable
import model.ErrorModel
import model.MarkTypeEntity
import model.PlaneModel

@Serializable
data class ResearchInitResponse(
  val response: ResearchInitModel? = null,
  val error: ErrorModel? = null,
)

@Serializable
data class ResearchInitModel(
  val CT: Map<String, SeriesModel>? = null,
  val MG: Map<String, SeriesModel>? = null,
  val DX: Map<String, SeriesModel>? = null,
  val dictionary: Map<String, MarkTypeEntity>? = null
)

@Serializable
data class SeriesModel(
  val name: String,
  val modalityModel: ModalityModel
)

@Serializable
data class ModalityModel(
  val planes: Map<String, PlaneModel>,
  val reversed: Boolean
)