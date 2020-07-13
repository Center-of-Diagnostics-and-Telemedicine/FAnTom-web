package model

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
  val CT: CTInitModel? = null,
  val MG: MGInitModel? = null,
  val DX: DXInitModel? = null
)

@Serializable
data class CTInitModel(
  val ct_axial: ModalityModel,
  val ct_frontal: ModalityModel,
  val ct_sagittal: ModalityModel,
  val reversed: Boolean
)

@Serializable
data class MGInitModel(
  val mg_lcc: ModalityModel,
  val mg_lmlo: ModalityModel,
  val mg_rcc: ModalityModel,
  val mg_rmlo: ModalityModel,
  val reversed: Boolean
)

@Serializable
data class DXInitModel(
  val dx_generic: ModalityModel,
  val dx_postero_anterior: ModalityModel,
  val dx_left_lateral: ModalityModel,
  val dx_right_lateral: ModalityModel,
  val reversed: Boolean
)

@Serializable
data class ModalityModel(
  val dicom_size_h: Int,
  val dicom_size_v: Int,
  val dicom_step_h: Double,
  val dicom_step_v: Double,
  val n_images: Int,
  val screen_size_h: Int,
  val screen_size_v: Int,
  val reversed: Boolean? = null
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
