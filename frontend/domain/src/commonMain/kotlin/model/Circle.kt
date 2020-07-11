package model

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class Circle(
  val dicomCenterX: Double,
  val dicomCenterY: Double,
  val dicomRadius: Double,
  val id: Int,
  val highlight: Boolean
)

fun MarkDomain.toCircle(cut: Cut, sliceNumber: Int): Circle? {
  markData.apply {
    when (cut.type) {
      CutType.Empty -> return null
      CutType.Axial -> {
        return if (sliceNumber < (z + radius) && sliceNumber > (z - radius)) {
          val horizontalRatio = cut.horizontalCutData.data.maxFramesSize.toDouble() / cut.data!!.height
          val verticalRatio = cut.verticalCutData.data.maxFramesSize.toDouble() / cut.data.height
          val coefficient = cut.verticalCutData.data.height.toDouble() / cut.data.maxFramesSize
          val x = x / verticalRatio
          val y = y / horizontalRatio
          val h = abs(sliceNumber - z) * coefficient
          val newRadius = sqrt((radius).pow(2) - (h).pow(2))
          Circle(
            dicomCenterX = x,
            dicomCenterY = y,
            dicomRadius = newRadius,
            id = id,
            highlight = selected
          )
        } else null
      }
      CutType.Frontal -> {
        return if ((sliceNumber < (y + radius)) && (sliceNumber > (y - radius))) {
          val horizontalRatio = cut.horizontalCutData.data.maxFramesSize.toDouble() / cut.data!!.height
          val verticalRatio = cut.verticalCutData.data.maxFramesSize.toDouble() / cut.data.maxFramesSize
          val resultX = x / verticalRatio
          val z = if (cut.data.reversed) cut.data.height - z else z
          val resultY = z / horizontalRatio
          val h = abs(sliceNumber - y)
          val newRadius = sqrt((radius).pow(2) - h.pow(2))
          Circle(
            dicomCenterX = resultX,
            dicomCenterY = resultY,
            dicomRadius = newRadius,
            id = id,
            highlight = selected
          )
        } else null

      }
      CutType.Sagittal -> {
        return if ((sliceNumber < (x + radius)) && (sliceNumber > (x - radius))) {
          val horizontalRatio = cut.horizontalCutData.data.maxFramesSize.toDouble() / cut.data!!.height
          val verticalRatio = cut.verticalCutData.data.maxFramesSize.toDouble() / cut.data.maxFramesSize
          val resultX = y / verticalRatio
          val z = if (cut.data.reversed) cut.data.height - z else z
          val resultY = z / horizontalRatio
          val h = abs(sliceNumber - x)
          val newRadius = sqrt((radius).pow(2) - h.pow(2))
          Circle(
            dicomCenterX = resultX,
            dicomCenterY = resultY,
            dicomRadius = newRadius,
            id = id,
            highlight = selected
          )
        } else null
      }
    }
  }
}