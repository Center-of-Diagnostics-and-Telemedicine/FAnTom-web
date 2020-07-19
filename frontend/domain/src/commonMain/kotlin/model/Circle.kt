package model

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class Circle(
  val dicomCenterX: Double,
  val dicomCenterY: Double,
  val dicomRadiusHorizontal: Double,
  val dicomRadiusVertical: Double,
  val id: Int,
  val highlight: Boolean,
  val isCenter: Boolean
)

fun MarkDomain.toCircle(cut: Cut, sliceNumber: Int): Circle? {
  markData.apply {
    when (cut.type) {
      CutType.EMPTY -> return null
      CutType.CT_AXIAL -> {
        val horizontalRatio = cut.horizontalCutData!!.data.n_images.toDouble() / cut.data.screen_size_h
        val verticalRatio = cut.verticalCutData!!.data.n_images.toDouble() / cut.data.screen_size_v
        return if (sliceNumber < (z + radiusHorizontal) && sliceNumber > (z - radiusHorizontal)) {
          val x = x / horizontalRatio
          val y = y / verticalRatio
          val h = abs(sliceNumber - z)// * coefficient
          val newRadius = sqrt((radiusHorizontal).pow(2) - (h).pow(2))
          Circle(
            dicomCenterX = x,
            dicomCenterY = y,
            dicomRadiusHorizontal = newRadius,
            dicomRadiusVertical = newRadius,
            id = id,
            highlight = selected,
            isCenter = sliceNumber == z.roundToInt()
          )
        } else null
      }
      CutType.CT_FRONTAL -> {
        return if ((sliceNumber < (y + radiusHorizontal)) && (sliceNumber > (y - radiusHorizontal))) {
          val horizontalRatio = cut.horizontalCutData!!.data.n_images.toDouble() / cut.data.screen_size_h
          val verticalRatio = cut.verticalCutData!!.data.n_images.toDouble() / cut.data.screen_size_v
          val resultX = x / horizontalRatio
          val z = if (cut.data.reversed == true) cut.data.screen_size_v - z else z
          val resultY = z / verticalRatio
          val h = abs(sliceNumber - y)
          val newRadius = sqrt((radiusHorizontal).pow(2) - h.pow(2))
          Circle(
            dicomCenterX = resultX,
            dicomCenterY = resultY,
            dicomRadiusHorizontal = newRadius,
            dicomRadiusVertical = newRadius,
            id = id,
            highlight = selected,
            isCenter = sliceNumber == y.roundToInt()
          )
        } else null

      }
      CutType.CT_SAGITTAL -> {
        return if ((sliceNumber < (x + radiusHorizontal)) && (sliceNumber > (x - radiusHorizontal))) {
          val horizontalRatio = cut.horizontalCutData!!.data.n_images.toDouble() / cut.data.screen_size_h
          val verticalRatio = cut.verticalCutData!!.data.n_images.toDouble() / cut.data.screen_size_v
          val resultX = y / horizontalRatio
          val z = if (cut.data.reversed == true) cut.data.screen_size_v - z else z
          val resultY = z / verticalRatio
          val h = abs(sliceNumber - x)
          val newRadius = sqrt((radiusHorizontal).pow(2) - h.pow(2))
          Circle(
            dicomCenterX = resultX,
            dicomCenterY = resultY,
            dicomRadiusHorizontal = newRadius,
            dicomRadiusVertical = newRadius,
            id = id,
            highlight = selected,
            isCenter = sliceNumber == x.roundToInt()
          )
        } else null
      }
      CutType.MG_RCC,
      CutType.MG_LCC,
      CutType.MG_RMLO,
      CutType.MG_LMLO -> {
        return if (markData.cutType == cut.type.intType) Circle(
          dicomCenterX = x,
          dicomCenterY = y,
          dicomRadiusHorizontal = radiusHorizontal,
          dicomRadiusVertical = radiusVertical,
          id = id,
          highlight = selected,
          isCenter = true
        ) else null
      }
      CutType.DX_GENERIC -> TODO()
      CutType.DX_POSTERO_ANTERIOR -> TODO()
      CutType.DX_LEFT_LATERAL -> TODO()
      CutType.DX_RIGHT_LATERAL -> TODO()
    }
  }
//  return null
}

fun List<Circle>.getCircleByPosition(
  dicomX: Double,
  dicomY: Double
): Circle? {
  return this
    .filter {
      val bottom = it.dicomCenterY + it.dicomRadiusVertical
      val top = it.dicomCenterY - it.dicomRadiusVertical
      val right = it.dicomCenterX + it.dicomRadiusHorizontal
      val left = it.dicomCenterX - it.dicomRadiusHorizontal
      val inVerticalBound = dicomY < bottom && dicomY > top
      val inHorizontalBound = dicomX < right && dicomX > left
      inVerticalBound && inHorizontalBound
    }
    .minBy {
      if (it.dicomRadiusHorizontal < it.dicomRadiusVertical) {
        it.dicomRadiusHorizontal
      } else {
        it.dicomRadiusVertical
      }
    }
}