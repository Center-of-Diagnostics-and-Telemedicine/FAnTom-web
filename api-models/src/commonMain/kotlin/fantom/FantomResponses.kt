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
  val ct_axial: FantomPlaneModel? = null,
  val ct_frontal: FantomPlaneModel? = null,
  val ct_sagittal: FantomPlaneModel? = null,
  val CT0: FantomPlaneModel? = null,
  val CT1: FantomPlaneModel? = null,
  val CT2: FantomPlaneModel? = null,
  val reversed: Boolean
)

@Serializable
data class FantomMGInitModel(
  val mg_lcc: FantomPlaneModel,
  val mg_lmlo: FantomPlaneModel,
  val mg_rcc: FantomPlaneModel,
  val mg_rmlo: FantomPlaneModel,
  val reversed: Boolean
)

@Serializable
data class FantomDXInitModel(
  val dx0: FantomPlaneModel,
  val reversed: Boolean
)

@Serializable
data class FantomPlaneModel(
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