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
  @SerialName("ct_axial")
  val ct_axial: ModalityModel,
  @SerialName("ct_frontal")
  val ct_frontal: ModalityModel,
  @SerialName("ct_sagittal")
  val ct_sagittal: ModalityModel,


//  val axialTomogram: Int,
//  val axialScreen: Int,
//  val frontalTomogram: Int,
//  val frontalScreen: Int,
//  val sagittalTomogram: Int? = 512,
//  val sagittalScreen: Int,
//  val pixelLength: Double,
  val reversed: Boolean
)

@Serializable
data class ModalityModel(
  @SerialName("dicom_size_h")
  val dicom_size_h: Int,
  @SerialName("dicom_size_v")
  val dicom_size_v: Int,
  @SerialName("dicom_step_h")
  val dicom_step_h: Double,
  @SerialName("dicom_step_v")
  val dicom_step_v: Double,
  @SerialName("n_images")
  val n_images: Int,
  @SerialName("screen_size_h")
  val screen_size_h: Int,
  @SerialName("screen_size_v")
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
