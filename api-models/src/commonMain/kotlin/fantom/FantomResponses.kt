package model.fantom

import kotlinx.serialization.Serializable
import model.ErrorModel

@Serializable
data class FantomResearchInitResponse(
  val response: FantomResearchInitModel? = null,
  val error: ErrorModel? = null,
  val dictionary: List<List<Map<String, FantomMarkTypeEntity>>>? = null
)

@Serializable
data class FantomResearchInitModel(
  val CT: FantomCTInitModel? = null,
  val MG: FantomMGInitModel? = null,
  val DX: FantomDXInitModel? = null,
  val dictionary: Map<String, FantomMarkTypeEntity>? = null
)

@Serializable
data class FantomMarkTypeEntity(
  val EN: String? = "",
  val RU: String? = "",
  val CLR: String? = ""
)

@Serializable
data class FantomCTInitModel(
  val ct_axial: FantomModalityModel? = null,
  val ct_frontal: FantomModalityModel? = null,
  val ct_sagittal: FantomModalityModel? = null,
  val CT0: FantomModalityModel? = null,
  val CT1: FantomModalityModel? = null,
  val CT2: FantomModalityModel? = null,
  val reversed: Boolean
)

@Serializable
data class FantomMGInitModel(
  val mg_lcc: FantomModalityModel,
  val mg_lmlo: FantomModalityModel,
  val mg_rcc: FantomModalityModel,
  val mg_rmlo: FantomModalityModel,
  val reversed: Boolean
)

@Serializable
data class FantomDXInitModel(
  val dx0: FantomModalityModel,
  val reversed: Boolean
)

@Serializable
data class FantomModalityModel(
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
data class FantomSliceModel(val image: String)

@Serializable
data class FantomSliceResponse(
  val response: FantomSliceModel? = null,
  val error: ErrorModel? = null
)


@Serializable
data class FantomHounsfieldModel(val brightness: Double?)

@Serializable
data class FantomHounsfieldResponse(
  val response: FantomHounsfieldModel? = null,
  val error: ErrorModel? = null
)