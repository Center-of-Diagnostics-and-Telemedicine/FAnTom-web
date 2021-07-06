package model

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

const val defaultMarkColor = "#00ff00"

interface Shape {
  val dicomCenterX: Double
  val dicomCenterY: Double
  val dicomRadiusHorizontal: Double
  val dicomRadiusVertical: Double
  val id: Int
  val highlight: Boolean
  val isCenter: Boolean
  val color: String
  val editable: Boolean
}

fun Shape.getType(): Int {
  return when (this) {
    is Rectangle -> SHAPE_TYPE_RECTANGLE
    is Circle -> SHAPE_TYPE_CIRCLE
    else -> throw NotImplementedError("Shape.getType not added")
  }
}

data class Circle(
  override val dicomCenterX: Double,
  override val dicomCenterY: Double,
  override val dicomRadiusHorizontal: Double,
  override val dicomRadiusVertical: Double,
  override val id: Int,
  override val highlight: Boolean,
  override val isCenter: Boolean,
  override val color: String,
  override val editable: Boolean = true
) : Shape

data class Rectangle(
  override val dicomCenterX: Double,
  override val dicomCenterY: Double,
  override val dicomRadiusHorizontal: Double,
  override val dicomRadiusVertical: Double,
  override val id: Int,
  override val highlight: Boolean,
  override val isCenter: Boolean,
  override val color: String,
  override val editable: Boolean = true
) : Shape

fun MarkModel.toShape(cut: Plane, sliceNumber: Int): Shape? {
  markData.apply {
    return when (shapeType) {
      SHAPE_TYPE_CIRCLE -> toCircle(cut, sliceNumber)
      SHAPE_TYPE_RECTANGLE -> toRectangle(cut)
      else -> null
    }
  }
}

fun List<Shape>.getShapeByPosition(dicomX: Double, dicomY: Double): Shape? {
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
    .minByOrNull {
      if (it.dicomRadiusHorizontal < it.dicomRadiusVertical) {
        it.dicomRadiusHorizontal
      } else {
        it.dicomRadiusVertical
      }
    }
}

private fun MarkModel.toCircle(cut: Plane, sliceNumber: Int): Circle? {
  markData.apply {
    when (cut.type) {
      CutType.EMPTY -> return null
      CutType.CT_AXIAL -> {
        val horizontalRatio =
          cut.horizontalCutData!!.data.nImages.toDouble() / cut.data.screenSizeH
        val verticalRatio = cut.verticalCutData!!.data.nImages.toDouble() / cut.data.screenSizeV
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
            isCenter = sliceNumber == z.roundToInt(),
            color = type?.color ?: ""
          )
        } else null
      }
      CutType.CT_FRONTAL -> {
        return if ((sliceNumber < (y + radiusHorizontal)) && (sliceNumber > (y - radiusHorizontal))) {
          val horizontalRatio =
            cut.horizontalCutData!!.data.nImages.toDouble() / cut.data.screenSizeH
          val verticalRatio =
            cut.verticalCutData!!.data.nImages.toDouble() / cut.data.screenSizeV
          val resultX = x / horizontalRatio
          val z = if (cut.data.reversed == true) cut.data.screenSizeV - z else z
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
            isCenter = sliceNumber == y.roundToInt(),
            color = type?.color ?: ""
          )
        } else null

      }
      CutType.CT_SAGITTAL -> {
        return if ((sliceNumber < (x + radiusHorizontal)) && (sliceNumber > (x - radiusHorizontal))) {
          val horizontalRatio =
            cut.horizontalCutData!!.data.nImages.toDouble() / cut.data.screenSizeH
          val verticalRatio =
            cut.verticalCutData!!.data.nImages.toDouble() / cut.data.screenSizeV
          val resultX = y / horizontalRatio
          val z = if (cut.data.reversed == true) cut.data.screenSizeV - z else z
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
            isCenter = sliceNumber == x.roundToInt(),
            color = type?.color ?: ""
          )
        } else null
      }
      CutType.DX_GENERIC,
      CutType.DX_POSTERO_ANTERIOR,
      CutType.DX_LEFT_LATERAL,
      CutType.DX_RIGHT_LATERAL,
      CutType.MG_RCC,
      CutType.MG_LCC,
      CutType.MG_RMLO,
      CutType.CT_0,
      CutType.CT_1,
      CutType.CT_2,
      CutType.MG_LMLO -> {
        return if (markData.cutType == cut.type.intType) Circle(
          dicomCenterX = x,
          dicomCenterY = y,
          dicomRadiusHorizontal = radiusHorizontal,
          dicomRadiusVertical = radiusVertical,
          id = id,
          highlight = selected,
          isCenter = true,
          color = type?.color ?: ""
        ) else null
      }
    }
  }
}

private fun MarkModel.toRectangle(cut: Plane): Rectangle? {
  markData.apply {
    when (cut.type) {
      CutType.EMPTY,
      CutType.CT_AXIAL,
      CutType.CT_FRONTAL,
      CutType.CT_SAGITTAL -> return null
      CutType.MG_RCC,
      CutType.MG_LCC,
      CutType.MG_RMLO,
      CutType.MG_LMLO,
      CutType.DX_GENERIC,
      CutType.DX_POSTERO_ANTERIOR,
      CutType.DX_LEFT_LATERAL,
      CutType.CT_0,
      CutType.CT_1,
      CutType.CT_2,
      CutType.DX_RIGHT_LATERAL -> {
        return if (markData.cutType == cut.type.intType) Rectangle(
          dicomCenterX = x,
          dicomCenterY = y,
          dicomRadiusHorizontal = radiusHorizontal,
          dicomRadiusVertical = radiusVertical,
          id = id,
          highlight = selected,
          isCenter = true,
          color = type?.color ?: "",
          editable = editable
        ) else null
      }
    }
  }
}