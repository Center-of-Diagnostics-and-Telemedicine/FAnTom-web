package model

import com.arkivanov.mvikotlin.core.utils.JvmSerializable

data class Cut(
  val type: CutType,
  val data: SliceSizeData?,
  val color: String,
  val verticalLineColor: String,
  val horizontalLineColor: String
) : JvmSerializable

sealed class CutType(val intType: Int) {
  object Empty : CutType(-1)
  object Axial : CutType(SLYCE_TYPE_AXIAL)
  object Frontal : CutType(SLYCE_TYPE_FRONTAL)
  object Sagittal : CutType(SLYCE_TYPE_SAGITTAL)
}

fun getColorByCutType(cutType: CutType): String {
  return when (cutType) {
    CutType.Empty -> TODO()
    CutType.Axial -> yellow
    CutType.Frontal -> pink
    CutType.Sagittal -> blue
  }
}
