package model

import com.arkivanov.mvikotlin.core.utils.JvmSerializable

data class Cut(
  val type: CutType,
  val data: ModalityModel,
  val color: String,
  val horizontalCutData: CutData?,
  val verticalCutData: CutData?,
  val researchType: ResearchType,
  val availableCutsForChange: List<CutType>
) : JvmSerializable

fun Cut.isPlanar(): Boolean = researchType != ResearchType.CT

enum class CutType(val intType: Int) {
  EMPTY(-1),
  CT_AXIAL(SLICE_TYPE_CT_AXIAL),
  CT_FRONTAL(SLICE_TYPE_CT_FRONTAL),
  CT_SAGITTAL(SLICE_TYPE_CT_SAGITTAL),
  CT_0(SLICE_TYPE_CT_0),
  CT_1(SLICE_TYPE_CT_1),
  CT_2(SLICE_TYPE_CT_2),
  MG_RCC(SLICE_TYPE_MG_RCC),
  MG_LCC(SLICE_TYPE_MG_LCC),
  MG_RMLO(SLICE_TYPE_MG_RMLO),
  MG_LMLO(SLICE_TYPE_MG_LMLO),
  DX_GENERIC(SLICE_TYPE_DX_GENERIC),
  DX_POSTERO_ANTERIOR(SLICE_TYPE_DX_POSTERO_ANTERIOR),
  DX_LEFT_LATERAL(SLICE_TYPE_DX_LEFT_LATERAL),
  DX_RIGHT_LATERAL(SLICE_TYPE_DX_RIGHT_LATERAL), ;

  companion object {
    private val VALUES = values()
    fun getByValue(value: Int) = VALUES.first { it.intType == value }
  }
}

fun CutType.isDoseReport(): Boolean {
  return when (this) {
    CutType.CT_0,
    CutType.CT_1,
    CutType.CT_2 -> true
    else -> false
  }
}

fun CutType.getName(): String? =
  when (this) {
    CutType.EMPTY -> null
    CutType.CT_AXIAL -> "Аксиальный"
    CutType.CT_FRONTAL -> "Фронтальный"
    CutType.CT_SAGITTAL -> "Сагиттальный"
    CutType.MG_RCC -> "MG_RCC"
    CutType.MG_LCC -> "MG_LCC"
    CutType.MG_RMLO -> "MG_RMLO"
    CutType.MG_LMLO -> "MG_LMLO"
    CutType.DX_GENERIC -> "DX_GENERIC"
    CutType.DX_POSTERO_ANTERIOR -> "DX_POSTERO_ANTERIOR"
    CutType.DX_LEFT_LATERAL -> "DX_LEFT_LATERAL"
    CutType.DX_RIGHT_LATERAL -> "DX_RIGHT_LATERAL"
    CutType.CT_0 -> "CT0"
    CutType.CT_1 -> "CT1"
    CutType.CT_2 -> "CT2"
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
    CutType.CT_0 -> yellow
    CutType.CT_1 -> pink
    CutType.CT_2 -> blue
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
      CutType.CT_0,
      CutType.CT_1,
      CutType.CT_2,
      CutType.DX_RIGHT_LATERAL -> PlanarPointPosition(x = dicomX, y = dicomY)
    }
  }
}

fun Cut.getMarkToSave(shape: Shape, sliceNumber: Int): MarkData? {
  return if (shape.dicomCenterX < 0.0 || shape.dicomCenterY < 0.0) {
    null
  } else {

    when (this.type) {
      CutType.EMPTY -> null
      CutType.CT_AXIAL -> {
        MarkData(
          x = (shape.dicomCenterX),
          y = (shape.dicomCenterY),
          z = sliceNumber.toDouble(),
          radiusHorizontal = shape.dicomRadiusHorizontal,
          radiusVertical = shape.dicomRadiusHorizontal,
          sizeVertical = shape.dicomRadiusHorizontal * data.dicom_step_h,
          sizeHorizontal = shape.dicomRadiusHorizontal * data.dicom_step_h,
          cutType = type.intType,
          shapeType = shape.getType()
        )
      }
      CutType.CT_FRONTAL -> {
        val verticalRatio = verticalCutData!!.data.n_images.toDouble() / data.screen_size_v
        val horizontalRatio = horizontalCutData!!.data.n_images.toDouble() / data.screen_size_h
        MarkData(
          x = (shape.dicomCenterX * horizontalRatio),
          y = sliceNumber.toDouble(),
          z = (shape.dicomCenterY * verticalRatio),
          radiusHorizontal = shape.dicomRadiusHorizontal,
          radiusVertical = shape.dicomRadiusHorizontal,
          sizeVertical = shape.dicomRadiusHorizontal * data.dicom_step_h,
          sizeHorizontal = shape.dicomRadiusHorizontal * data.dicom_step_h,
          cutType = type.intType,
          shapeType = shape.getType()
        )
      }
      CutType.CT_SAGITTAL -> {
        val verticalRatio = verticalCutData!!.data.n_images.toDouble() / data.screen_size_v
        val horizontalRatio = horizontalCutData!!.data.n_images.toDouble() / data.screen_size_h
        MarkData(
          x = sliceNumber.toDouble(),
          y = (shape.dicomCenterX * horizontalRatio),
          z = (shape.dicomCenterY * verticalRatio),
          radiusHorizontal = shape.dicomRadiusHorizontal,
          radiusVertical = shape.dicomRadiusHorizontal,
          sizeVertical = shape.dicomRadiusHorizontal * data.dicom_step_h,
          sizeHorizontal = shape.dicomRadiusHorizontal * data.dicom_step_h,
          cutType = type.intType,
          shapeType = shape.getType()
        )
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
        MarkData(
          x = shape.dicomCenterX,
          y = shape.dicomCenterY,
          z = -1.0,
          radiusHorizontal = shape.dicomRadiusHorizontal,
          radiusVertical = shape.dicomRadiusVertical,
          sizeVertical = shape.dicomRadiusVertical * data.dicom_step_v * 2,
          sizeHorizontal = shape.dicomRadiusHorizontal * data.dicom_step_h * 2,
          cutType = type.intType,
          shapeType = shape.getType()
        )
      }
    }
  }
}

fun Cut.getSliceNumberByMark(mark: MarkModel): Int? {
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
    CutType.CT_0 -> null
    CutType.CT_1 -> null
    CutType.CT_2 -> null
  }
}

fun Cut.updateCoordinates(mark: MarkModel, deltaX: Double, deltaY: Double): MarkModel? {
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
      mark.copy(
        markData = markData.copy(
          y = markData.y + deltaY,
          x = markData.x + deltaX
        )
      ).also { it.selected = true }
    }
  }
}

fun Cut.updateCoordinatesByRect(
  mark: MarkModel,
  deltaX: Double,
  deltaY: Double,
  rect: Rect
): MarkModel? {
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
      when (rect.type) {
        MoveRectType.TOP -> {
          val radius = markData.radiusVertical - deltaY / 2
          val newY = markData.y + deltaY / 2
          mark.copy(
            markData = markData.copy(
              radiusVertical = radius,
              y = newY,
              sizeVertical = radius * data.dicom_step_v * 2
            ),
          ).also { it.selected = true }
        }
        MoveRectType.BOTTOM -> {
          val radius = markData.radiusVertical + deltaY / 2
          val newY = markData.y + deltaY / 2
          mark.copy(
            markData = markData.copy(
              radiusVertical = radius,
              y = newY,
              sizeVertical = radius * data.dicom_step_v * 2
            )
          ).also { it.selected = true }
        }
        MoveRectType.RIGHT -> {
          val radius = markData.radiusHorizontal + deltaX / 2
          val newX = markData.x + deltaX / 2
          mark.copy(
            markData = markData.copy(
              radiusHorizontal = radius,
              x = newX,
              sizeHorizontal = radius * data.dicom_step_h * 2
//                size = (it.radius + deltaX) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            )
          ).also { it.selected = true }
        }
        MoveRectType.LEFT -> {
          val radius = markData.radiusHorizontal - deltaX / 2
          val newX = markData.x + deltaX / 2
          mark.copy(
            markData = markData.copy(
              radiusHorizontal = radius,
              x = newX,
              sizeHorizontal = radius * data.dicom_step_h * 2
//                size = (it.radius - deltaX) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            )
          ).also { it.selected = true }
        }
        MoveRectType.LEFT_TOP -> {
          val radiusVertical = markData.radiusVertical - deltaY / 2
          val newY = markData.y + deltaY / 2
          val radiusHorizontal = markData.radiusHorizontal - deltaX / 2
          val newX = markData.x + deltaX / 2
          mark.copy(
            markData = markData.copy(
              radiusHorizontal = radiusHorizontal,
              radiusVertical = radiusVertical,
              x = newX,
              y = newY,
              sizeVertical = radiusVertical * data.dicom_step_v * 2,
              sizeHorizontal = radiusHorizontal * data.dicom_step_h * 2
            )
          ).also { it.selected = true }
        }
        MoveRectType.RIGHT_TOP -> {
          val radiusVertical = markData.radiusVertical - deltaY / 2
          val newY = markData.y + deltaY / 2
          val radiusHorizontal = markData.radiusHorizontal + deltaX / 2
          val newX = markData.x + deltaX / 2
          mark.copy(
            markData = markData.copy(
              radiusHorizontal = radiusHorizontal,
              radiusVertical = radiusVertical,
              x = newX,
              y = newY,
              sizeVertical = radiusVertical * data.dicom_step_v * 2,
              sizeHorizontal = radiusHorizontal * data.dicom_step_h * 2
//                size = (it.radius + deltaX) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            )
          ).also { it.selected = true }
        }
        MoveRectType.LEFT_BOTTOM -> {
          val radiusVertical = markData.radiusVertical + deltaY / 2
          val newY = markData.y + deltaY / 2
          val radiusHorizontal = markData.radiusHorizontal - deltaX / 2
          val newX = markData.x + deltaX / 2
          mark.copy(
            markData = markData.copy(
              radiusHorizontal = radiusHorizontal,
              radiusVertical = radiusVertical,
              x = newX,
              y = newY,
              sizeVertical = radiusVertical * data.dicom_step_v * 2,
              sizeHorizontal = radiusHorizontal * data.dicom_step_h * 2
//                size = (it.radius - deltaX) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            )
          ).also { it.selected = true }
        }
        MoveRectType.RIGHT_BOTTOM -> {
          val radiusVertical = markData.radiusVertical + deltaY / 2
          val newY = markData.y + deltaY / 2
          val radiusHorizontal = markData.radiusHorizontal + deltaX / 2
          val newX = markData.x + deltaX / 2
          mark.copy(
            markData = markData.copy(
              radiusHorizontal = radiusHorizontal,
              radiusVertical = radiusVertical,
              x = newX,
              y = newY,
              sizeVertical = radiusVertical * data.dicom_step_v * 2,
              sizeHorizontal = radiusHorizontal * data.dicom_step_h * 2
//                size = (it.radius + deltaX) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            )
          ).also { it.selected = true }
        }
      }
    }
  }
}
