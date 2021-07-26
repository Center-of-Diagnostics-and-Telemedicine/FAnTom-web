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
  val dicomSizeH: Int,
  val dicomSizeV: Int,
  val dicomStepH: Double = 1.0,
  val dicomStepV: Double = 1.0,
  val nImages: Int,
  val screenSizeH: Int = 512,
  val screenSizeV: Int = 512,
  val reversed: Boolean? = null,
  val SOPInstanceUID: String? = null,
  val seriesInstanceUid: String? = null,
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
