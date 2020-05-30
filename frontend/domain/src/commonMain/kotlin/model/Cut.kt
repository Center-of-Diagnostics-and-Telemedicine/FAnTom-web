package model

import com.arkivanov.mvikotlin.core.utils.JvmSerializable

data class Cut(
  val type: CutType,
  val data: SliceSizeData?
) : JvmSerializable

sealed class CutType(val intType: Int) {
  object Empty : CutType(-1)
  object Axial : CutType(SLYCE_TYPE_AXIAL)
  object Frontal : CutType(SLYCE_TYPE_FRONTAL)
  object Sagittal : CutType(SLYCE_TYPE_SAGITTAL)
}
