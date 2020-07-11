package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResearchInitModel(
  val axialReal: Int,
  val axialInterpolated: Int,
  val frontalReal: Int,
  val frontalInterpolated: Int,
  val sagittalReal: Int,
  val sagittalInterpolated: Int,
  val pixelLength: Double,
  val reversed: Boolean
)

@Serializable
data class ResearchInitResponse(
  val response: ResearchInitModel? = null,
  val error: ErrorModel? = null
)

@Serializable
data class ResearchInitResponseNew(
  val response: ResearchInitModelNew? = null,
  val error: ErrorModel? = null
)

@Serializable
data class ResearchInitModelNew(
  val CT: CTInitModel? = null
)

@Serializable
data class CTInitModel(
  val axialTomogram: Int,
  val axialScreen: Int,
  val frontalTomogram: Int,
  val frontalScreen: Int,
  val sagittalTomogram: Int? = 512,
  val sagittalScreen: Int,
  val pixelLength: Double,
  val reversed: Boolean
)

@Serializable
data class ResearchesModel(val researches: List<Research>)

@Serializable
data class ResearchesResponse(
  val response: ResearchesModel? = null,
  val error: ErrorModel? = null
)


@Serializable
data class SliceModel(val image: String)

@Serializable
data class SliceResponse(
  val response: SliceModel? = null,
  val error: ErrorModel? = null
)


@Serializable
data class HounsfieldModel(val huValue: Double)

@Serializable
data class HounsfieldResponse(
  val response: HounsfieldModel? = null,
  val error: ErrorModel? = null
)