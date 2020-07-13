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
    val horizontalRatio = cut.horizontalCutData.data.n_images.toDouble() / cut.data!!.screen_size_h
    val verticalRatio = cut.verticalCutData.data.n_images.toDouble() / cut.data.screen_size_v
    when (cut.type) {
      CutType.Empty -> return null
      CutType.Axial -> {
        return if (sliceNumber < (z + radius) && sliceNumber > (z - radius)) {
          val x = x / horizontalRatio
          val y = y / verticalRatio
          val h = abs(sliceNumber - z)// * coefficient
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
          val resultX = x / horizontalRatio
          val z = if (cut.data.reversed == true) cut.data.screen_size_v - z else z
          val resultY = z / verticalRatio
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
          val resultX = y / horizontalRatio
          val z = if (cut.data.reversed == true) cut.data.screen_size_v - z else z
          val resultY = z / verticalRatio
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
//  return null
}