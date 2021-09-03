package model.fantom

import kotlinx.serialization.Serializable
import model.ErrorModel

@Serializable
data class FantomResearchInitResponse(
  val response: FantomResearchInitModel? = null,
  val error: ErrorModel? = null,
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

sealed interface FantomModalityInitModel {
  val dimensions: Map<String, FantomPlaneModel>
  val reversed: Boolean
}

@Serializable
data class FantomCTInitModel(
  override val dimensions: Map<String, FantomPlaneModel>,
  override val reversed: Boolean
) : FantomModalityInitModel

@Serializable
data class FantomMGInitModel(
  override val dimensions: Map<String, FantomPlaneModel>,
  override val reversed: Boolean
) : FantomModalityInitModel

@Serializable
data class FantomDXInitModel(
  override val dimensions: Map<String, FantomPlaneModel>,
  override val reversed: Boolean
) : FantomModalityInitModel

@Serializable
data class FantomPlaneModel(
  val dicom_size_h: Int,
  val dicom_size_v: Int,
  val dicom_step_h: Double = 1.0,
  val dicom_step_v: Double = 1.0,
  val n_images: Int,
  val screen_size_h: Int = 512,
  val screen_size_v: Int = 512,
  val series_instance_uid: String,
  val type: String? = null,
  val window_center: Int,
  val window_width: Int,
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