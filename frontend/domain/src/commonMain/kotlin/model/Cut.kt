package model

import com.arkivanov.mvikotlin.core.utils.JvmSerializable

data class Cut(
  val type: CutType,
  val data: ModalityModel,
  val color: String,
  val horizontalCutData: CutData?,
  val verticalCutData: CutData?,
  val researchType: ResearchType
) : JvmSerializable

fun Cut.isPlanar(): Boolean = researchType != ResearchType.CT

enum class CutType(val intType: Int) {
  EMPTY(-1),
  CT_AXIAL(SLICE_TYPE_CT_AXIAL),
  CT_FRONTAL(SLICE_TYPE_CT_FRONTAL),
  CT_SAGITTAL(SLICE_TYPE_CT_SAGITTAL),
  MG_RCC(SLICE_TYPE_MG_RCC),
  MG_LCC(SLICE_TYPE_MG_LCC),
  MG_RMLO(SLICE_TYPE_MG_RMLO),
  MG_LMLO(SLICE_TYPE_MG_LMLO),
  DX_GENERIC(SLICE_TYPE_DX_GENERIC),
  DX_POSTERO_ANTERIOR(SLICE_TYPE_DX_POSTERO_ANTERIOR),
  DX_LEFT_LATERAL(SLICE_TYPE_DX_LEFT_LATERAL),
  DX_RIGHT_LATERAL(SLICE_TYPE_DX_RIGHT_LATERAL),
}

data class CutData(
  val type: CutType,
  val data: ModalityModel,
  val color: String
)

val axialColor = getColorByCutType(CutType.CT_AXIAL)
val frontalColor = getColorByCutType(CutType.CT_FRONTAL)
val sagittalColor = getColorByCutType(CutType.CT_SAGITTAL)
val rcc_color = getColorByCutType(CutType.MG_RCC)
val lcc_color = getColorByCutType(CutType.MG_LCC)
val rmlo_color = getColorByCutType(CutType.MG_RMLO)
val lmlo_color = getColorByCutType(CutType.MG_LMLO)
val generic_color = getColorByCutType(CutType.DX_GENERIC)
val postero_color = getColorByCutType(CutType.DX_POSTERO_ANTERIOR)
val left_lateral_color = getColorByCutType(CutType.DX_LEFT_LATERAL)
val right_lateral_color = getColorByCutType(CutType.DX_RIGHT_LATERAL)

fun getColorByCutType(cutType: CutType): String {
  return when (cutType) {
    CutType.EMPTY -> TODO()
    CutType.CT_AXIAL -> yellow
    CutType.CT_FRONTAL -> pink
    CutType.CT_SAGITTAL -> blue
    CutType.MG_RCC -> yellow
    CutType.MG_LCC -> pink
    CutType.MG_RMLO -> blue
    CutType.MG_LMLO -> green
    CutType.DX_GENERIC -> yellow
    CutType.DX_POSTERO_ANTERIOR -> pink
    CutType.DX_LEFT_LATERAL -> blue
    CutType.DX_RIGHT_LATERAL -> green
  }
}

fun Cut.getPosition(dicomX: Double, dicomY: Double, sliceNumber: Int): PointPosition? {
  if (dicomX < 0.0 || dicomY < 0.0) {
    return null
  } else {
    return when (this.type) {
      CutType.EMPTY -> null
      CutType.CT_AXIAL -> {
        MultiPlanarPointPosition(
          x = dicomX,
          y = dicomY,
          z = sliceNumber.toDouble()
        )
      }
      CutType.CT_FRONTAL -> {
        val verticalRatio = verticalCutData!!.data.n_images.toDouble() / data.screen_size_v
        val horizontalRatio = horizontalCutData!!.data.n_images.toDouble() / data.screen_size_h
        MultiPlanarPointPosition(
          x = dicomX * horizontalRatio,
          y = sliceNumber.toDouble(),
          z = dicomY * verticalRatio
        )
      }
      CutType.CT_SAGITTAL -> {
        val verticalRatio = verticalCutData!!.data.n_images.toDouble() / data.screen_size_v
        val horizontalRatio = horizontalCutData!!.data.n_images.toDouble() / data.screen_size_h
        MultiPlanarPointPosition(
          x = sliceNumber.toDouble(),
          y = dicomX * horizontalRatio,
          z = dicomY * verticalRatio
        )
      }
      CutType.MG_RCC,
      CutType.MG_LCC,
      CutType.MG_RMLO,
      CutType.MG_LMLO,
      CutType.DX_GENERIC,
      CutType.DX_POSTERO_ANTERIOR,
      CutType.DX_LEFT_LATERAL,
      CutType.DX_RIGHT_LATERAL -> PlanarPointPosition(x = dicomX, y = dicomY)
    }
  }
}

fun Cut.getMarkToSave(circle: Circle, sliceNumber: Int): MarkData? {
  return if (circle.dicomCenterX < 0.0 || circle.dicomCenterY < 0.0) {
    null
  } else {

    when (this.type) {
      CutType.EMPTY -> null
      CutType.CT_AXIAL -> {
        MarkData(
          x = (circle.dicomCenterX),
          y = (circle.dicomCenterY),
          z = sliceNumber.toDouble(),
          radiusHorizontal = circle.dicomRadiusHorizontal,
          radiusVertical = circle.dicomRadiusHorizontal,
          size = 0.0,
          cutType = type.intType
        )
      }
      CutType.CT_FRONTAL -> {
        val verticalRatio = verticalCutData!!.data.n_images.toDouble() / data.screen_size_v
        val horizontalRatio = horizontalCutData!!.data.n_images.toDouble() / data.screen_size_h
        MarkData(
          x = (circle.dicomCenterX * horizontalRatio),
          y = sliceNumber.toDouble(),
          z = (circle.dicomCenterY * verticalRatio),
          radiusHorizontal = circle.dicomRadiusHorizontal,
          radiusVertical = circle.dicomRadiusHorizontal,
          size = 0.0,
          cutType = type.intType
        )
      }
      CutType.CT_SAGITTAL -> {
        val verticalRatio = verticalCutData!!.data.n_images.toDouble() / data.screen_size_v
        val horizontalRatio = horizontalCutData!!.data.n_images.toDouble() / data.screen_size_h
        MarkData(
          x = sliceNumber.toDouble(),
          y = (circle.dicomCenterX * horizontalRatio),
          z = (circle.dicomCenterY * verticalRatio),
          radiusHorizontal = circle.dicomRadiusHorizontal,
          radiusVertical = circle.dicomRadiusHorizontal,
          size = 0.0,
          cutType = type.intType
        )
      }
      CutType.MG_RCC,
      CutType.MG_LCC,
      CutType.MG_RMLO,
      CutType.MG_LMLO -> {
        MarkData(
          x = circle.dicomCenterX,
          y = circle.dicomCenterY,
          z = -1.0,
          radiusHorizontal = circle.dicomRadiusHorizontal,
          radiusVertical = circle.dicomRadiusVertical,
          size = 0.0,
          cutType = type.intType
        )
      }
      CutType.DX_GENERIC -> TODO()
      CutType.DX_POSTERO_ANTERIOR -> TODO()
      CutType.DX_LEFT_LATERAL -> TODO()
      CutType.DX_RIGHT_LATERAL -> TODO()
    }
  }
}

fun Cut.getSliceNumberByMark(mark: MarkDomain): Int? {
  return when (type) {
    CutType.EMPTY -> null
    CutType.CT_AXIAL -> mark.markData.z.toInt()
    CutType.CT_FRONTAL -> mark.markData.y.toInt()
    CutType.CT_SAGITTAL -> mark.markData.x.toInt()
    CutType.MG_RCC -> null
    CutType.MG_LCC -> null
    CutType.MG_RMLO -> null
    CutType.MG_LMLO -> null
    CutType.DX_GENERIC -> null
    CutType.DX_POSTERO_ANTERIOR -> null
    CutType.DX_LEFT_LATERAL -> null
    CutType.DX_RIGHT_LATERAL -> null
  }
}

fun Cut.updateCoordinates(mark: MarkDomain, deltaX: Double, deltaY: Double): MarkDomain? {
  val markData = mark.markData
  return when (type) {
    CutType.EMPTY -> null
    CutType.CT_AXIAL -> {
      val verticalRatio = verticalCutData!!.data.n_images.toDouble() / data.screen_size_v
      val horizontalRatio = horizontalCutData!!.data.n_images.toDouble() / data.screen_size_h
      mark.copy(
        markData = markData.copy(
          x = markData.x + deltaX * horizontalRatio,
          y = markData.y + deltaY * verticalRatio
        ),
      ).also { it.selected = true }
    }
    CutType.CT_FRONTAL -> {
      val verticalRatio = verticalCutData!!.data.n_images.toDouble() / data.screen_size_v
      val horizontalRatio = horizontalCutData!!.data.n_images.toDouble() / data.screen_size_h
      mark.copy(
        markData = markData.copy(
          x = markData.x + deltaX * horizontalRatio,
          z = markData.z + deltaY * verticalRatio
        )
      ).also { it.selected = true }
    }
    CutType.CT_SAGITTAL -> {
      val verticalRatio = verticalCutData!!.data.n_images.toDouble() / data.screen_size_v
      val horizontalRatio = horizontalCutData!!.data.n_images.toDouble() / data.screen_size_h
      mark.copy(
        markData = markData.copy(
          y = markData.y + deltaX * horizontalRatio,
          z = markData.z + deltaY * verticalRatio
        )
      ).also { it.selected = true }
    }
    CutType.MG_RCC,
    CutType.MG_LCC,
    CutType.MG_RMLO,
    CutType.MG_LMLO -> {
      mark.copy(
        markData = markData.copy(
          y = markData.y + deltaY,
          x = markData.x + deltaX
        )
      ).also { it.selected = true }
    }
    CutType.DX_GENERIC -> TODO()
    CutType.DX_POSTERO_ANTERIOR -> TODO()
    CutType.DX_LEFT_LATERAL -> TODO()
    CutType.DX_RIGHT_LATERAL -> TODO()
  }
}

fun Cut.updateCoordinates(
  mark: MarkDomain,
  deltaX: Double,
  deltaY: Double,
  rect: Rect
): MarkDomain? {
  val markData = mark.markData
  return when (type) {
    CutType.EMPTY -> null
    CutType.CT_AXIAL,
    CutType.CT_FRONTAL,
    CutType.CT_SAGITTAL -> {
      val verticalRatio = verticalCutData!!.data.n_images.toDouble() / data.screen_size_v
      val horizontalRatio = horizontalCutData!!.data.n_images.toDouble() / data.screen_size_h
      when (rect.type) {
        MoveRectType.TOP -> {
          val radius = markData.radiusVertical - deltaY * verticalRatio
          mark.copy(
            markData = markData.copy(
              radiusVertical = radius,
              radiusHorizontal = radius
//                size = (it.radius - deltaY) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            ),
          ).also { it.selected = true }
        }
        MoveRectType.BOTTOM -> {
          val radius = markData.radiusVertical + deltaY * verticalRatio
          mark.copy(
            markData = markData.copy(
              radiusVertical = radius,
              radiusHorizontal = radius
//                size = (it.radius + deltaY) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            )
          ).also { it.selected = true }
        }
        MoveRectType.RIGHT -> {
          val radius = markData.radiusHorizontal + deltaX * horizontalRatio
          mark.copy(
            markData = markData.copy(
              radiusHorizontal = radius,
              radiusVertical = radius
//                size = (it.radius + deltaX) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            )
          ).also { it.selected = true }
        }
        MoveRectType.LEFT -> {
          val radius = markData.radiusHorizontal - deltaX * horizontalRatio
          mark.copy(
            markData = markData.copy(
              radiusHorizontal = radius,
              radiusVertical = radius
//                size = (it.radius - deltaX) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            )
          ).also { it.selected = true }
        }
        MoveRectType.LEFT_TOP -> null
        MoveRectType.RIGHT_TOP -> null
        MoveRectType.LEFT_BOTTOM -> null
        MoveRectType.RIGHT_BOTTOM -> null
      }
    }
    CutType.MG_RCC,
    CutType.MG_LCC,
    CutType.MG_RMLO,
    CutType.MG_LMLO -> {
      val verticalRatio = verticalCutData!!.data.n_images.toDouble() / data.screen_size_v
      val horizontalRatio = horizontalCutData!!.data.n_images.toDouble() / data.screen_size_h
      when (rect.type) {
        MoveRectType.TOP -> {
          val radius = markData.radiusVertical - deltaY * verticalRatio
          mark.copy(
            markData = markData.copy(
              radiusVertical = radius,
              radiusHorizontal = radius
//                size = (it.radius - deltaY) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            ),
          ).also { it.selected = true }
        }
        MoveRectType.BOTTOM -> {
          val radius = markData.radiusVertical + deltaY * verticalRatio
          mark.copy(
            markData = markData.copy(
              radiusVertical = radius,
              radiusHorizontal = radius
//                size = (it.radius + deltaY) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            )
          ).also { it.selected = true }
        }
        MoveRectType.RIGHT -> {
          val radius = markData.radiusHorizontal + deltaX * horizontalRatio
          mark.copy(
            markData = markData.copy(
              radiusHorizontal = radius,
              radiusVertical = radius
//                size = (it.radius + deltaX) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            )
          ).also { it.selected = true }
        }
        MoveRectType.LEFT -> {
          val radius = markData.radiusHorizontal - deltaX * horizontalRatio
          mark.copy(
            markData = markData.copy(
              radiusHorizontal = radius,
              radiusVertical = radius
//                size = (it.radius - deltaX) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            )
          ).also { it.selected = true }
        }
        MoveRectType.LEFT_TOP -> {
          val radiusVertical = markData.radiusVertical - deltaY * verticalRatio
          val radiusHorizontal = markData.radiusHorizontal - deltaX * horizontalRatio
          mark.copy(
            markData = markData.copy(
              radiusHorizontal = radiusHorizontal,
              radiusVertical = radiusVertical
//                size = (it.radius - deltaX) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            )
          ).also { it.selected = true }
        }
        MoveRectType.RIGHT_TOP -> {
          val radiusVertical = markData.radiusVertical - deltaY * verticalRatio
          val radiusHorizontal = markData.radiusHorizontal + deltaX * horizontalRatio
          mark.copy(
            markData = markData.copy(
              radiusHorizontal = radiusHorizontal,
              radiusVertical = radiusVertical
//                size = (it.radius + deltaX) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            )
          ).also { it.selected = true }
        }
        MoveRectType.LEFT_BOTTOM -> {
          val radiusVertical = markData.radiusVertical + deltaY * verticalRatio
          val radiusHorizontal = markData.radiusHorizontal - deltaX * horizontalRatio
          mark.copy(
            markData = markData.copy(
              radiusHorizontal = radiusHorizontal,
              radiusVertical = radiusVertical
//                size = (it.radius - deltaX) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            )
          ).also { it.selected = true }
        }
        MoveRectType.RIGHT_BOTTOM -> {
          val radiusVertical = markData.radiusVertical + deltaY * verticalRatio
          val radiusHorizontal = markData.radiusHorizontal + deltaX * horizontalRatio
          mark.copy(
            markData = markData.copy(
              radiusHorizontal = radiusHorizontal,
              radiusVertical = radiusVertical
//                size = (it.radius + deltaX) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            )
          ).also { it.selected = true }
        }
      }
    }
    CutType.DX_GENERIC -> TODO()
    CutType.DX_POSTERO_ANTERIOR -> TODO()
    CutType.DX_LEFT_LATERAL -> TODO()
    CutType.DX_RIGHT_LATERAL -> TODO()
  }
}


fun Cut.updateCircle(oldCircle: Circle, newCircle: Circle): Circle? {
  return when (type) {
    CutType.EMPTY -> null
    CutType.CT_AXIAL -> newCircle.copy(
      dicomCenterX = newCircle.dicomCenterX,
      dicomCenterY = newCircle.dicomCenterY
    )
    CutType.CT_FRONTAL -> newCircle.copy(
      dicomCenterX = newCircle.dicomCenterX,
      dicomCenterY = newCircle.dicomCenterY
    )
    CutType.CT_SAGITTAL -> newCircle.copy(
      dicomCenterY = newCircle.dicomCenterX
    )
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
