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
      CutType.EMPTY -> return null
      CutType.CT_AXIAL -> {
        val horizontalRatio = cut.horizontalCutData!!.data.n_images.toDouble() / cut.data!!.screen_size_h
        val verticalRatio = cut.verticalCutData!!.data.n_images.toDouble() / cut.data.screen_size_v
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
      CutType.CT_FRONTAL -> {
        return if ((sliceNumber < (y + radius)) && (sliceNumber > (y - radius))) {
          val horizontalRatio = cut.horizontalCutData!!.data.n_images.toDouble() / cut.data!!.screen_size_h
          val verticalRatio = cut.verticalCutData!!.data.n_images.toDouble() / cut.data.screen_size_v
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
      CutType.CT_SAGITTAL -> {
        return if ((sliceNumber < (x + radius)) && (sliceNumber > (x - radius))) {
          val horizontalRatio = cut.horizontalCutData!!.data.n_images.toDouble() / cut.data!!.screen_size_h
          val verticalRatio = cut.verticalCutData!!.data.n_images.toDouble() / cut.data.screen_size_v
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
      CutType.MG_RCC -> TODO()
      CutType.MG_LCC -> TODO()
      CutType.MG_RMLO -> TODO()
      CutType.MG_LMLO -> TODO()
      CutType.DX_GENERIC -> TODO()
      CutType.DX_POSTERO_ANTERIOR -> TODO()
      CutType.DX_LEFT_LATERAL -> TODO()
      CutType.DX_RIGHT_LATERAL -> TODO()
    }
  }
//  return null
}