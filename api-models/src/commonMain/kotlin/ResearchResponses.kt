package model

import kotlinx.serialization.SerialName
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
  val ct_axial: PlaneModel? = null,
  val ct_frontal: PlaneModel? = null,
  val ct_sagittal: PlaneModel? = null,
  val CT0: PlaneModel? = null,
  val CT1: PlaneModel? = null,
  val CT2: PlaneModel? = null,
  val reversed: Boolean
)

@Serializable
data class MGInitModel(
  val mg_lcc: PlaneModel,
  val mg_lmlo: PlaneModel,
  val mg_rcc: PlaneModel,
  val mg_rmlo: PlaneModel,
  val reversed: Boolean
)

@Serializable
data class DXInitModel(
  val dx0: PlaneModel,
  val reversed: Boolean
)

@Serializable
data class PlaneModel(
  @SerialName("dicom_size_h")
  val dicomSizeH: Int,
  @SerialName("dicom_size_v")
  val dicomSizeV: Int,
  @SerialName("dicom_step_h")
  val dicomStepH: Double = 1.0,
  @SerialName("dicom_step_v")
  val dicomStepV: Double = 1.0,
  @SerialName("n_images")
  val nImages: Int,
  @SerialName("screen_size_h")
  val screenSizeH: Int = 512,
  @SerialName("screen_size_v")
  val screenSizeV: Int = 512,
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
