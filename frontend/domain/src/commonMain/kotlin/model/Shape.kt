package model

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

const val defaultMarkColor = "#00ff00"

interface Shape {
  val dicomX: Double
  val dicomY: Double
  val dicomWidth: Double
  val dicomHeight: Double
  val id: Int
  val highlight: Boolean
  val isCenter: Boolean
  val color: String
  val editable: Boolean
}

fun Shape.getType(): Int {
  return when (this) {
    is RectangleModel -> SHAPE_TYPE_RECTANGLE
    is CircleModel -> SHAPE_TYPE_CIRCLE
    is EllipseModel -> SHAPE_TYPE_ELLIPSE
    else -> throw NotImplementedError("Shape.getType not added")
  }
}

data class CircleModel(
  override val dicomX: Double,
  override val dicomY: Double,
  override val dicomWidth: Double,
  override val dicomHeight: Double,
  override val id: Int,
  override val highlight: Boolean,
  override val isCenter: Boolean,
  override val color: String,
  override val editable: Boolean = true
) : Shape {
  constructor(dicomX: Double, dicomY: Double) : this(
    dicomX = dicomX,
    dicomY = dicomY,
    dicomWidth = 0.0,
    dicomHeight = 0.0,
    id = -1,
    highlight = false,
    isCenter = false,
    color = ""
  )
}

data class EllipseModel(
  override val dicomX: Double,
  override val dicomY: Double,
  override val dicomWidth: Double,
  override val dicomHeight: Double,
  override val id: Int,
  override val highlight: Boolean,
  override val isCenter: Boolean,
  override val color: String,
  override val editable: Boolean = true
) : Shape {

  constructor(dicomX: Double, dicomY: Double) : this(
    dicomX = dicomX,
    dicomY = dicomY,
    dicomWidth = 0.0,
    dicomHeight = 0.0,
    id = -1,
    highlight = false,
    isCenter = false,
    color = ""
  )
}

data class RectangleModel(
  override val dicomX: Double,
  override val dicomY: Double,
  override val dicomWidth: Double,
  override val dicomHeight: Double,
  override val id: Int,
  override val highlight: Boolean,
  override val isCenter: Boolean,
  override val color: String,
  override val editable: Boolean = true
) : Shape {
  constructor(dicomX: Double, dicomY: Double) : this(
    dicomX = dicomX,
    dicomY = dicomY,
    dicomWidth = 0.0,
    dicomHeight = 0.0,
    id = -1,
    highlight = false,
    isCenter = false,
    color = ""
  )
}

fun MarkModel.toShape(cut: Plane, sliceNumber: Int): Shape? {
  markData.apply {
    return when (shapeType) {
      SHAPE_TYPE_CIRCLE -> toCircle(cut, sliceNumber)
      SHAPE_TYPE_RECTANGLE -> toRectangle(cut)
      SHAPE_TYPE_ELLIPSE -> toEllipse(cut)
      else -> null
    }
  }
}

fun List<Shape>.getShapeByPosition(dicomX: Double, dicomY: Double): Shape? {
  return this
    .filter {
      val bottom = it.dicomY + it.dicomHeight
      val top = it.dicomY - it.dicomHeight
      val right = it.dicomX + it.dicomWidth
      val left = it.dicomX - it.dicomWidth
      val inVerticalBound = dicomY < bottom && dicomY > top
      val inHorizontalBound = dicomX < right && dicomX > left
      inVerticalBound && inHorizontalBound
    }
    .minByOrNull {
      if (it.dicomWidth < it.dicomHeight) {
        it.dicomWidth
      } else {
        it.dicomHeight
      }
    }
}

private fun MarkModel.toCircle(cut: Plane, sliceNumber: Int): CircleModel? {
  markData.apply {
    val colorByCutType = getColorByCutType(cut.type)
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
          CircleModel(
            dicomX = x,
            dicomY = y,
            dicomWidth = newRadius,
            dicomHeight = newRadius,
            id = id,
            highlight = selected,
            isCenter = sliceNumber == z.roundToInt(),
            color = type?.color ?: colorByCutType
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
          CircleModel(
            dicomX = resultX,
            dicomY = resultY,
            dicomWidth = newRadius,
            dicomHeight = newRadius,
            id = id,
            highlight = selected,
            isCenter = sliceNumber == y.roundToInt(),
            color = type?.color ?: colorByCutType
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
          CircleModel(
            dicomX = resultX,
            dicomY = resultY,
            dicomWidth = newRadius,
            dicomHeight = newRadius,
            id = id,
            highlight = selected,
            isCenter = sliceNumber == x.roundToInt(),
            color = type?.color ?: colorByCutType
          )
        } else null
      }
      CutType.CT_0,
      CutType.CT_1,
      CutType.CT_2,
      CutType.DX_GENERIC,
      CutType.DX_POSTERO_ANTERIOR,
      CutType.DX_LEFT_LATERAL,
      CutType.DX_RIGHT_LATERAL,
      CutType.MG_RCC,
      CutType.MG_LCC,
      CutType.MG_RMLO,
      CutType.MG_LMLO -> {
        return if (markData.cutType == cut.type.intType) CircleModel(
          dicomX = x,
          dicomY = y,
          dicomWidth = radiusHorizontal,
          dicomHeight = radiusVertical,
          id = id,
          highlight = selected,
          isCenter = true,
          color = type?.color ?: colorByCutType
        ) else null
      }
      CutType.CT_UNKNOWN -> TODO()
      CutType.CT_DOSE_REPORT_UNKNOWN -> TODO()
      CutType.MG_UNKNOWN -> TODO()
      CutType.DX_UNKNOWN -> TODO()
    }
  }
}

private fun MarkModel.toRectangle(cut: Plane): RectangleModel? {
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
        return if (markData.cutType == cut.type.intType) RectangleModel(
          dicomX = x,
          dicomY = y,
          dicomWidth = radiusHorizontal * 2,
          dicomHeight = radiusVertical * 2,
          id = id,
          highlight = selected,
          isCenter = true,
          color = type?.color ?: getColorByCutType(cut.type),
          editable = editable
        ) else null
      }
      CutType.CT_UNKNOWN -> TODO()
      CutType.CT_DOSE_REPORT_UNKNOWN -> TODO()
      CutType.MG_UNKNOWN -> TODO()
      CutType.DX_UNKNOWN -> TODO()
    }
  }
}

private fun MarkModel.toEllipse(cut: Plane): EllipseModel? {
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
        return if (markData.cutType == cut.type.intType) EllipseModel(
          dicomX = x,
          dicomY = y,
          dicomWidth = radiusHorizontal * 2,
          dicomHeight = radiusVertical * 2,
          id = id,
          highlight = selected,
          isCenter = true,
          color = type?.color ?: getColorByCutType(cut.type),
          editable = editable
        ) else null
      }
      CutType.CT_UNKNOWN -> TODO()
      CutType.CT_DOSE_REPORT_UNKNOWN -> TODO()
      CutType.MG_UNKNOWN -> TODO()
      CutType.DX_UNKNOWN -> TODO()
    }
  }
}