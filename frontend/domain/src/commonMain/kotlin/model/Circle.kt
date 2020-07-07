package model

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class Circle(
  val dicomCenterX: Double,
  val dicomCenterY: Double,
  val dicomRadius: Double
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
          val x = x.toDouble() / verticalRatio
          val y = y.toDouble() / horizontalRatio
          val h = abs(sliceNumber - z).toDouble() * coefficient
          val newRadius = sqrt((radius).pow(2) - (h).pow(2))
          Circle(dicomCenterX = x, dicomCenterY = y, dicomRadius = newRadius)
        } else null
      }
      CutType.Frontal -> {
        return if ((sliceNumber < (y + radius)) && (sliceNumber > (y - radius))) {
          val horizontalRatio = cut.horizontalCutData.data.maxFramesSize.toDouble() / cut.data!!.height
          val verticalRatio = cut.verticalCutData.data.maxFramesSize.toDouble() / cut.data.maxFramesSize
          val resultX = x.toDouble() / verticalRatio
          //val z = if (reversed) height - area.z else area.z
          val resultY = z.toDouble() / horizontalRatio
          val h = abs(sliceNumber - y).toDouble()
          val newRadius = sqrt((radius).pow(2) - h.pow(2))
          Circle(dicomCenterX = resultX, dicomCenterY = resultY, dicomRadius = newRadius)
        } else null

      }
      CutType.Sagittal -> {
        return if ((sliceNumber < (x + radius)) && (sliceNumber > (x - radius))) {
          val horizontalRatio = cut.horizontalCutData.data.maxFramesSize.toDouble() / cut.data!!.height
          val verticalRatio = cut.verticalCutData.data.maxFramesSize.toDouble() / cut.data.maxFramesSize
          val resultX = y.toDouble() / verticalRatio
          //val z = if (reversed) height - area.z else area.z
          val resultY = z.toDouble() / horizontalRatio
          val h = abs(sliceNumber - x).toDouble()
          val newRadius = sqrt((radius).pow(2) - h.pow(2))
          Circle(dicomCenterX = resultX, dicomCenterY = resultY, dicomRadius = newRadius)
        } else null
      }
    }
  }
}