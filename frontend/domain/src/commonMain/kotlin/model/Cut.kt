package model

import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kotlin.math.roundToInt

data class Cut(
  val type: CutType,
  val data: SliceSizeData?,
  val color: String,
  val verticalCutData: CutData,
  val horizontalCutData: CutData
) : JvmSerializable

enum class CutType(val intType: Int) {
  Empty(-1),
  Axial(SLYCE_TYPE_AXIAL),
  Frontal(SLYCE_TYPE_FRONTAL),
  Sagittal(SLYCE_TYPE_SAGITTAL)
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

fun Cut.getMarkToSave(circle: Circle, sliceNumber: Int): MarkData? {
  return if (circle.dicomCenterX < 0.0 || circle.dicomCenterY < 0.0) {
    null
  } else {
    when (this.type) {
      CutType.Empty -> null
      CutType.Axial -> {
        val horizontalRatio = horizontalCutData.data.maxFramesSize.toDouble() / data!!.height
        val verticalRatio = verticalCutData.data.maxFramesSize.toDouble() / data.height
        MarkData(
          x = (circle.dicomCenterX * verticalRatio).roundToInt(),
          y = (circle.dicomCenterY * horizontalRatio).roundToInt(),
          z = sliceNumber,
          radius = circle.dicomRadius,
          size = 0.0
        )
      }
      CutType.Frontal -> {
        val horizontalRatio = horizontalCutData.data.maxFramesSize.toDouble() / data!!.height
        val verticalRatio = verticalCutData.data.maxFramesSize.toDouble() / data.maxFramesSize
        MarkData(
          x = (circle.dicomCenterX * verticalRatio).roundToInt(),
          y = sliceNumber,
          z = (circle.dicomCenterY * horizontalRatio).roundToInt(),
          radius = circle.dicomRadius,
          size = 0.0
        )
      }
      CutType.Sagittal -> {
        val horizontalRatio = horizontalCutData.data.maxFramesSize.toDouble() / data!!.height
        val verticalRatio = verticalCutData.data.maxFramesSize.toDouble() / data.maxFramesSize
        MarkData(
          x = sliceNumber,
          y = (circle.dicomCenterX * verticalRatio).roundToInt(),
          z = (circle.dicomCenterY * horizontalRatio).roundToInt(),
          radius = circle.dicomRadius,
          size = 0.0
        )
      }
    }
  }
}

fun Cut.getSliceNumberByMark(mark: MarkDomain): Int? {
  return when (type) {
    CutType.Empty -> null
    CutType.Axial -> mark.markData.z
    CutType.Frontal
    -> mark.markData.y
    CutType.Sagittal -> mark.markData.x
  }
}
