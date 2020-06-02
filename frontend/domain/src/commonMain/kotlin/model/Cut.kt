package model

import com.arkivanov.mvikotlin.core.utils.JvmSerializable

data class Cut(
  val type: CutType,
  val data: SliceSizeData?,
  val color: String,
  val verticalCutData: CutData,
  val horizontalCutData: CutData
) : JvmSerializable

sealed class CutType(val intType: Int) {
  object Empty : CutType(-1)
  object Axial : CutType(SLYCE_TYPE_AXIAL)
  object Frontal : CutType(SLYCE_TYPE_FRONTAL)
  object Sagittal : CutType(SLYCE_TYPE_SAGITTAL)
}

data class CutData(
  val type: CutType,
  val data: SliceSizeData,
  val color: String
)

val axialColor = getColorByCutType(CutType.Axial)
val frontalColor = getColorByCutType(CutType.Frontal)
val sagittalColor = getColorByCutType(CutType.Sagittal)

fun getColorByCutType(cutType: CutType): String {
  return when (cutType) {
    CutType.Empty -> TODO()
    CutType.Axial -> yellow
    CutType.Frontal -> pink
    CutType.Sagittal -> blue
  }
}

fun Cut.getPosition(dicomX: Double, dicomY: Double, sliceNumber: Int): PointPosition? {
  if (dicomX < 0.0 || dicomY < 0.0) {
    return null
  } else {
    return when (this.type) {
      CutType.Empty -> TODO()
      CutType.Axial -> {
        val horizontalRatio = horizontalCutData.data.maxFramesSize.toDouble() / data!!.height
        val verticalRatio = verticalCutData.data.maxFramesSize.toDouble() / data.height
        PointPosition(
          x = dicomX * verticalRatio,
          y = dicomY * horizontalRatio,
          z = sliceNumber.toDouble()
        )
      }
      CutType.Frontal -> {
        val horizontalRatio = horizontalCutData.data.maxFramesSize.toDouble() / data!!.height
        val verticalRatio = verticalCutData.data.maxFramesSize.toDouble() / data.maxFramesSize
        PointPosition(
          x = dicomX * verticalRatio,
          y = sliceNumber.toDouble(),
          z = dicomY * horizontalRatio
        )
      }
      CutType.Sagittal -> {
        val horizontalRatio = horizontalCutData.data.maxFramesSize.toDouble() / data!!.height
        val verticalRatio = verticalCutData.data.maxFramesSize.toDouble() / data.maxFramesSize
        PointPosition(
          x = sliceNumber.toDouble(),
          y = dicomX * verticalRatio,
          z = dicomY * horizontalRatio
        )
      }
    }
  }
}

fun Cut.buildCircle(circle: Circle, externalCutType: CutType): Circle {
  return when (externalCutType) {
    type -> circle
    verticalCutData.type -> circle
    horizontalCutData.type -> circle
    else -> circle
  }
}
