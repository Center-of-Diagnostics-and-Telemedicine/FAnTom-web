package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SliceRequest(
  val black: Int,
  val white: Int,
  val gamma: Double,
  val sliceType: Int,
  val mipMethod: Int,
  val sliceNumber: Int,
  val mipValue: Int
)

@Serializable
data class HounsfieldRequest(
  val axialCoord: Int,
  val frontalCoord: Int,
  val sagittalCoord: Int
)

@Serializable
data class HounsfieldRequestNew(
  val image: ImageModel,
  val point: PointModel
)

@Serializable
data class ConfirmCTTypeRequest(
  val researchId: Int,
  val ctType: Int,
  val leftPercent: Int,
  val rightPercent: Int
)

@Serializable
data class SliceRequestNew(
  val image: ImageModel,
  val brightness: BrightnessModel
)

@Serializable
data class ImageModel(
  val modality: String,
  val type: String,
  val number: Int,
  val mip: MipModel
)

@Serializable
data class MipModel(
  val mip_method: String,
  val mip_value: Int
)

@Serializable
data class BrightnessModel(
  val black: Int,
  val white: Int,
  val gamma: Double
)

@Serializable
data class PointModel(
  val vertical: Int,
  val horizontal: Int
)

fun getModalityStringType(type: Int): String {
  return when (type) {
    SLYCE_TYPE_AXIAL -> "ct_axial"
    SLYCE_TYPE_FRONTAL -> "ct_frontal"
    SLYCE_TYPE_SAGITTAL -> "ct_sagittal"
    else -> throw NotImplementedError("Not implemented sliceType for type $type")
  }
}

fun getMipMethodStringType(type: Int): String {
  return when (type) {
    MIP_METHOD_TYPE_NO_MIP -> "mip_no_mip"
    MIP_METHOD_TYPE_AVERAGE -> "mip_average"
    MIP_METHOD_TYPE_MAXVALUE -> "mip_maxvalue"
    MIP_METHOD_TYPE_MINVALUE -> "mip_minvalue"
    else -> throw NotImplementedError("Not mipmethod implemented for type $type")
  }
}
