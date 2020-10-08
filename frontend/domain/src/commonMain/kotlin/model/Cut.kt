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
  MG_RCC(SLICE_TYPE_MG_RCC),
  MG_LCC(SLICE_TYPE_MG_LCC),
  MG_RMLO(SLICE_TYPE_MG_RMLO),
  MG_LMLO(SLICE_TYPE_MG_LMLO),
  DX_GENERIC(SLICE_TYPE_DX_GENERIC),
  DX_POSTERO_ANTERIOR(SLICE_TYPE_DX_POSTERO_ANTERIOR),
  DX_LEFT_LATERAL(SLICE_TYPE_DX_LEFT_LATERAL),
  DX_RIGHT_LATERAL(SLICE_TYPE_DX_RIGHT_LATERAL),
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
          sizeVertical = circle.dicomRadiusHorizontal * data.dicom_step_h,
          sizeHorizontal = circle.dicomRadiusHorizontal * data.dicom_step_h,
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
          sizeVertical = circle.dicomRadiusHorizontal * data.dicom_step_h,
          sizeHorizontal = circle.dicomRadiusHorizontal * data.dicom_step_h,
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
          sizeVertical = circle.dicomRadiusHorizontal * data.dicom_step_h,
          sizeHorizontal = circle.dicomRadiusHorizontal * data.dicom_step_h,
          cutType = type.intType
        )
      }
      CutType.DX_GENERIC,
      CutType.DX_POSTERO_ANTERIOR,
      CutType.DX_LEFT_LATERAL,
      CutType.DX_RIGHT_LATERAL,
      CutType.MG_RCC,
      CutType.MG_LCC,
      CutType.MG_RMLO,
      CutType.MG_LMLO -> {
        println("MY: circle.dicomRadiusHorizontal = ${circle.dicomRadiusHorizontal}, circle.dicomRadiusVertical = ${circle.dicomRadiusVertical}")
        println("MY: data.dicom_step_v = ${data.dicom_step_v}, data.dicom_step_h = ${data.dicom_step_h}")
        println("MY: circle.dicomRadiusVertical * data.dicom_step_v = ${circle.dicomRadiusVertical * data.dicom_step_v}, circle.dicomRadiusHorizontal * data.dicom_step_h = ${circle.dicomRadiusHorizontal * data.dicom_step_h}")
        MarkData(
          x = circle.dicomCenterX,
          y = circle.dicomCenterY,
          z = -1.0,
          radiusHorizontal = circle.dicomRadiusHorizontal,
          radiusVertical = circle.dicomRadiusVertical,
          sizeVertical = circle.dicomRadiusVertical * data.dicom_step_v * 2,
          sizeHorizontal = circle.dicomRadiusHorizontal * data.dicom_step_h * 2,
          cutType = type.intType
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
