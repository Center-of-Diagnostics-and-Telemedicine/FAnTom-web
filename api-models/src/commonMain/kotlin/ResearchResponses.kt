package model

import kotlinx.serialization.Serializable

@Serializable
data class ResearchInitResponseNew(
  val response: ResearchInitModelNew? = null,
  val error: ErrorModel? = null,
  val dictionary: List<List<Map<String, MarkTypeEntity>>>? = null
)

@Serializable
data class ResearchInitModelNew(
  val CT: CTInitModel? = null,
  val MG: MGInitModel? = null,
  val DX: DXInitModel? = null,
  val dictionary: Map<String, MarkTypeEntity>? = null
)

@Serializable
data class MarkTypeEntity(
  val EN: String? = "",
  val RU: String? = "",
  val CLR: String? = ""
)

@Serializable
data class CTInitModel(
  val ct_axial: ModalityModel? = null,
  val ct_frontal: ModalityModel? = null,
  val ct_sagittal: ModalityModel? = null,
  val CT0: ModalityModel? = null,
  val CT1: ModalityModel? = null,
  val CT2: ModalityModel? = null,
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
  val dx0: ModalityModel,
  val reversed: Boolean
)

@Serializable
data class ModalityModel(
  val dicom_size_h: Int,
  val dicom_size_v: Int,
  val dicom_step_h: Double = 1.0,
  val dicom_step_v: Double = 1.0,
  val n_images: Int,
  val screen_size_h: Int = 512,
  val screen_size_v: Int = 512,
  val reversed: Boolean? = null,
  val SOPInstanceUID: String? = null,
  val file: String? = null
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
data class HounsfieldModel(val brightness: Double?)

@Serializable
data class HounsfieldResponse(
  val response: HounsfieldModel? = null,
  val error: ErrorModel? = null
)
