package model

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
  val mip: MipModel,
  val width: Int = 1,
  val height: Int = 1
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

fun getSliceStringType(type: Int): String {
  return when (type) {
    SLICE_TYPE_CT_AXIAL -> "ct_axial"
    SLICE_TYPE_CT_FRONTAL -> "ct_frontal"
    SLICE_TYPE_CT_SAGITTAL -> "ct_sagittal"
    SLICE_TYPE_MG_RCC -> "mg_rcc"
    SLICE_TYPE_MG_LCC -> "mg_lcc"
    SLICE_TYPE_MG_RMLO -> "mg_rmlo"
    SLICE_TYPE_MG_LMLO -> "mg_lmlo"
    SLICE_TYPE_DX_GENERIC -> "dx_generic"
    SLICE_TYPE_DX_POSTERO_ANTERIOR -> "dx_postero_anterior"
    SLICE_TYPE_DX_LEFT_LATERAL -> "dx_left_lateral"
    SLICE_TYPE_DX_RIGHT_LATERAL -> "dx_right_lateral"
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

fun getModalityStringType(type: Int): String {
  return when (type) {
    SLICE_TYPE_CT_AXIAL,
    SLICE_TYPE_CT_FRONTAL,
    SLICE_TYPE_CT_SAGITTAL -> CT_RESEARCH_TYPE
    SLICE_TYPE_MG_RCC,
    SLICE_TYPE_MG_LCC,
    SLICE_TYPE_MG_RMLO,
    SLICE_TYPE_MG_LMLO -> MG_RESEARCH_TYPE
    SLICE_TYPE_DX_GENERIC,
    SLICE_TYPE_DX_POSTERO_ANTERIOR,
    SLICE_TYPE_DX_LEFT_LATERAL,
    SLICE_TYPE_DX_RIGHT_LATERAL -> DX_RESEARCH_TYPE
    else -> throw NotImplementedError("Not implemented modality for type $type")
  }
}
