package model

import com.arkivanov.mvikotlin.core.utils.JvmSerializable

data class Plane(
  val type: CutType,
  val data: PlaneModel,
  val color: String,
  val researchType: ResearchType,
  val horizontalCutData: CutData?,
  val verticalCutData: CutData?,
  val availableCutsForChange: List<CutType>
) : JvmSerializable

fun Plane.isPlanar(): Boolean = researchType != ResearchType.CT

enum class CutType(val intType: Int) {
  EMPTY(-1),
  CT_AXIAL(SLICE_TYPE_CT_AXIAL),
  CT_FRONTAL(SLICE_TYPE_CT_FRONTAL),
  CT_SAGITTAL(SLICE_TYPE_CT_SAGITTAL),
  CT_UNKNOWN(SLICE_TYPE_CT_UNKNOWN),
  CT_0(SLICE_TYPE_CT_0),
  CT_1(SLICE_TYPE_CT_1),
  CT_2(SLICE_TYPE_CT_2),
  CT_DOSE_REPORT_UNKNOWN(SLICE_TYPE_DOSE_REPORT_UNKNOWN),
  MG_RCC(SLICE_TYPE_MG_RCC),
  MG_LCC(SLICE_TYPE_MG_LCC),
  MG_RMLO(SLICE_TYPE_MG_RMLO),
  MG_LMLO(SLICE_TYPE_MG_LMLO),
  MG_UNKNOWN(SLICE_TYPE_MG_UNKNOWN),
  DX_GENERIC(SLICE_TYPE_DX_GENERIC),
  DX_POSTERO_ANTERIOR(SLICE_TYPE_DX_POSTERO_ANTERIOR),
  DX_LEFT_LATERAL(SLICE_TYPE_DX_LEFT_LATERAL),
  DX_RIGHT_LATERAL(SLICE_TYPE_DX_RIGHT_LATERAL),
  DX_UNKNOWN(SLICE_TYPE_DX_UNKNOWN), ;

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
    CutType.CT_UNKNOWN -> TODO()
    CutType.CT_DOSE_REPORT_UNKNOWN -> TODO()
    CutType.MG_UNKNOWN -> TODO()
    CutType.DX_UNKNOWN -> TODO()
  }


data class CutData(
  val type: CutType,
  val data: PlaneModel,
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
    CutType.CT_UNKNOWN -> green
    CutType.CT_DOSE_REPORT_UNKNOWN ->  green
    CutType.MG_UNKNOWN ->  green
    CutType.DX_UNKNOWN ->  green
  }
}

fun Plane.getPosition(dicomX: Double, dicomY: Double, sliceNumber: Int): PointPosition? {
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
        val verticalRatio = verticalCutData!!.data.nImages.toDouble() / data.screenSizeV
        val horizontalRatio = horizontalCutData!!.data.nImages.toDouble() / data.screenSizeH
        MultiPlanarPointPosition(
          x = dicomX * horizontalRatio,
          y = sliceNumber.toDouble(),
          z = dicomY * verticalRatio
        )
      }
      CutType.CT_SAGITTAL -> {
        val verticalRatio = verticalCutData!!.data.nImages.toDouble() / data.screenSizeV
        val horizontalRatio = horizontalCutData!!.data.nImages.toDouble() / data.screenSizeH
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
      CutType.DX_RIGHT_LATERAL -> PointPositionModel(x = dicomX, y = dicomY)
      CutType.CT_UNKNOWN -> TODO()
      CutType.CT_DOSE_REPORT_UNKNOWN -> TODO()
      CutType.MG_UNKNOWN -> TODO()
      CutType.DX_UNKNOWN -> TODO()
    }
  }
}

fun Plane.getMarkToSave(shape: Shape, sliceNumber: Int): MarkData? {
  return if (shape.dicomX < 0.0 || shape.dicomY < 0.0) {
    null
  } else {

    when (this.type) {
      CutType.EMPTY -> null
      CutType.CT_AXIAL -> {
        MarkData(
          x = (shape.dicomX),
          y = (shape.dicomY),
          z = sliceNumber.toDouble(),
          radiusHorizontal = shape.dicomWidth,
          radiusVertical = shape.dicomWidth,
          sizeVertical = shape.dicomWidth * data.dicomStepH,
          sizeHorizontal = shape.dicomWidth * data.dicomStepH,
          cutType = type.intType,
          shapeType = shape.getType()
        )
      }
      CutType.CT_FRONTAL -> {
        val verticalRatio = verticalCutData!!.data.nImages.toDouble() / data.screenSizeV
        val horizontalRatio = horizontalCutData!!.data.nImages.toDouble() / data.screenSizeH
        MarkData(
          x = (shape.dicomX * horizontalRatio),
          y = sliceNumber.toDouble(),
          z = (shape.dicomY * verticalRatio),
          radiusHorizontal = shape.dicomWidth,
          radiusVertical = shape.dicomWidth,
          sizeVertical = shape.dicomWidth * data.dicomStepH,
          sizeHorizontal = shape.dicomWidth * data.dicomStepH,
          cutType = type.intType,
          shapeType = shape.getType()
        )
      }
      CutType.CT_SAGITTAL -> {
        val verticalRatio = verticalCutData!!.data.nImages.toDouble() / data.screenSizeV
        val horizontalRatio = horizontalCutData!!.data.nImages.toDouble() / data.screenSizeH
        MarkData(
          x = sliceNumber.toDouble(),
          y = (shape.dicomX * horizontalRatio),
          z = (shape.dicomY * verticalRatio),
          radiusHorizontal = shape.dicomWidth,
          radiusVertical = shape.dicomWidth,
          sizeVertical = shape.dicomWidth * data.dicomStepH,
          sizeHorizontal = shape.dicomWidth * data.dicomStepH,
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
          x = shape.dicomX,
          y = shape.dicomY,
          z = -1.0,
          radiusHorizontal = shape.dicomWidth / 2,
          radiusVertical = shape.dicomHeight / 2,
          sizeVertical = shape.dicomHeight * data.dicomStepV * 2,
          sizeHorizontal = shape.dicomWidth * data.dicomStepH * 2,
          cutType = type.intType,
          shapeType = shape.getType()
        )
      }
      CutType.CT_UNKNOWN -> TODO()
      CutType.CT_DOSE_REPORT_UNKNOWN -> TODO()
      CutType.MG_UNKNOWN -> TODO()
      CutType.DX_UNKNOWN -> TODO()
    }
  }
}

fun Plane.getSliceNumberByMark(mark: MarkModel): Int? {
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
    CutType.CT_UNKNOWN -> null
    CutType.CT_DOSE_REPORT_UNKNOWN -> null
    CutType.MG_UNKNOWN -> null
    CutType.DX_UNKNOWN -> null
  }
}

fun Plane.updateCoordinates(mark: MarkModel, deltaX: Double, deltaY: Double): MarkModel? {
  val markData = mark.markData
  return when (type) {
    CutType.EMPTY -> null
    CutType.CT_AXIAL -> {
      val verticalRatio = verticalCutData!!.data.nImages.toDouble() / data.screenSizeV
      val horizontalRatio = horizontalCutData!!.data.nImages.toDouble() / data.screenSizeH
      mark.copy(
        markData = markData.copy(
          x = markData.x + deltaX * horizontalRatio,
          y = markData.y + deltaY * verticalRatio
        ),
      ).also { it.selected = true }
    }
    CutType.CT_FRONTAL -> {
      val verticalRatio = verticalCutData!!.data.nImages.toDouble() / data.screenSizeV
      val horizontalRatio = horizontalCutData!!.data.nImages.toDouble() / data.screenSizeH
      mark.copy(
        markData = markData.copy(
          x = markData.x + deltaX * horizontalRatio,
          z = markData.z + deltaY * verticalRatio
        )
      ).also { it.selected = true }
    }
    CutType.CT_SAGITTAL -> {
      val verticalRatio = verticalCutData!!.data.nImages.toDouble() / data.screenSizeV
      val horizontalRatio = horizontalCutData!!.data.nImages.toDouble() / data.screenSizeH
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
    CutType.CT_UNKNOWN -> TODO()
    CutType.CT_DOSE_REPORT_UNKNOWN -> TODO()
    CutType.MG_UNKNOWN -> TODO()
    CutType.DX_UNKNOWN -> TODO()
  }
}

fun Plane.updateCoordinatesByRect(
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
      val verticalRatio = verticalCutData!!.data.nImages.toDouble() / data.screenSizeV
      val horizontalRatio = horizontalCutData!!.data.nImages.toDouble() / data.screenSizeH
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
              sizeVertical = radius * data.dicomStepV * 2
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
              sizeVertical = radius * data.dicomStepV * 2
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
              sizeHorizontal = radius * data.dicomStepH * 2
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
              sizeHorizontal = radius * data.dicomStepH * 2
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
              sizeVertical = radiusVertical * data.dicomStepV * 2,
              sizeHorizontal = radiusHorizontal * data.dicomStepH * 2
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
              sizeVertical = radiusVertical * data.dicomStepV * 2,
              sizeHorizontal = radiusHorizontal * data.dicomStepH * 2
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
              sizeVertical = radiusVertical * data.dicomStepV * 2,
              sizeHorizontal = radiusHorizontal * data.dicomStepH * 2
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
              sizeVertical = radiusVertical * data.dicomStepV * 2,
              sizeHorizontal = radiusHorizontal * data.dicomStepH * 2
//                size = (it.radius + deltaX) * 2 * axialSlicesSizesDataObservable.value.pixelLength
            )
          ).also { it.selected = true }
        }
      }
    }
    CutType.CT_UNKNOWN -> TODO()
    CutType.CT_DOSE_REPORT_UNKNOWN -> TODO()
    CutType.MG_UNKNOWN -> TODO()
    CutType.DX_UNKNOWN -> TODO()
  }
}


private val ctCuts = listOf(
  CutType.CT_AXIAL,
  CutType.CT_FRONTAL,
  CutType.CT_SAGITTAL
)

private val mgCuts = listOf(
  CutType.MG_RCC,
  CutType.MG_LCC,
  CutType.MG_LMLO,
  CutType.MG_RMLO
)

private val dxCuts = listOf(
  CutType.DX_GENERIC,
  CutType.DX_POSTERO_ANTERIOR,
  CutType.DX_LEFT_LATERAL,
  CutType.DX_RIGHT_LATERAL
)

private val doseReportCuts = listOf(
  CutType.CT_0,
  CutType.CT_1,
  CutType.CT_2
)

fun buildPlane(type: CutType, data: ResearchDataModel): Plane =
  when (type) {
    CutType.EMPTY -> emptyCut(data)
    CutType.CT_AXIAL -> axialCut(data, ctCuts.filter { it != type })
    CutType.CT_FRONTAL -> frontalCut(data, ctCuts.filter { it != type })
    CutType.CT_SAGITTAL -> sagittalCut(data, ctCuts.filter { it != type })
    CutType.MG_RCC -> mgRcc(data, mgCuts.filter { it != type })
    CutType.MG_LCC -> mgLcc(data, mgCuts.filter { it != type })
    CutType.MG_RMLO -> mgRmlo(data, mgCuts.filter { it != type })
    CutType.MG_LMLO -> mgLmlo(data, mgCuts.filter { it != type })
    CutType.DX_GENERIC -> dxGeneric(data, dxCuts.filter { it != type })
    CutType.DX_POSTERO_ANTERIOR -> dxPosteroAnterior(data, dxCuts.filter { it != type })
    CutType.DX_LEFT_LATERAL -> dxLeftLateral(data, dxCuts.filter { it != type })
    CutType.DX_RIGHT_LATERAL -> dxRightLateral(data, dxCuts.filter { it != type })
    CutType.CT_0 -> ct0(data, doseReportCuts.filter { it != type })
    CutType.CT_1 -> ct1(data, doseReportCuts.filter { it != type })
    CutType.CT_2 -> ct2(data, doseReportCuts.filter { it != type })
    CutType.CT_UNKNOWN -> TODO()
    CutType.CT_DOSE_REPORT_UNKNOWN -> TODO()
    CutType.MG_UNKNOWN -> TODO()
    CutType.DX_UNKNOWN -> TODO()
  }

private fun emptyCut(data: ResearchDataModel): Plane =
  Plane(
    type = CutType.EMPTY,
    data = PlaneModel(0, 0, 0.0, 0.0, 0, 0, 0),
    color = "",
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = listOf()
  )

private fun axialCut(data: ResearchDataModel, types: List<CutType>): Plane =
  Plane(
    type = CutType.CT_AXIAL,
    data = data.planes[CT_AXIAL_STRING]
      ?: error("CutsContainerStoreFactory: AXIAL NOT FOUND IN DATA"),
    color = axialColor,
    verticalCutData = CutData(
      type = CutType.CT_FRONTAL,
      data = data.planes[CT_FRONTAL_STRING]
        ?: error("CutsContainerStoreFactory: FRONTAL NOT FOUND IN DATA"),
      color = frontalColor
    ),
    horizontalCutData = CutData(
      type = CutType.CT_SAGITTAL,
      data = data.planes[CT_SAGITTAL_STRING]
        ?: error("CutsContainerStoreFactory: SAGITTAL NOT FOUND IN DATA"),
      color = sagittalColor
    ),
    researchType = data.type,
    availableCutsForChange = types
  )

private fun frontalCut(data: ResearchDataModel, types: List<CutType>): Plane =
  Plane(
    type = CutType.CT_FRONTAL,
    data = data.planes[CT_FRONTAL_STRING]
      ?: error("CutsContainerStoreFactory: FRONTAL NOT FOUND IN DATA"),
    color = frontalColor,
    verticalCutData = CutData(
      type = CutType.CT_AXIAL,
      data = data.planes[CT_AXIAL_STRING]
        ?: error("CutsContainerStoreFactory: AXIAL NOT FOUND IN DATA"),
      color = axialColor
    ),
    horizontalCutData = CutData(
      type = CutType.CT_SAGITTAL,
      data = data.planes[CT_SAGITTAL_STRING]
        ?: error("CutsContainerStoreFactory: SAGITTAL NOT FOUND IN DATA"),
      color = sagittalColor
    ),
    researchType = data.type,
    availableCutsForChange = types
  )

private fun sagittalCut(data: ResearchDataModel, types: List<CutType>): Plane =
  Plane(
    type = CutType.CT_SAGITTAL,
    data = data.planes[CT_SAGITTAL_STRING]
      ?: error("CutsContainerStoreFactory: SAGITTAL NOT FOUND IN DATA"),
    color = sagittalColor,
    verticalCutData = CutData(
      type = CutType.CT_AXIAL,
      data = data.planes[CT_AXIAL_STRING]
        ?: error("CutsContainerStoreFactory: AXIAL NOT FOUND IN DATA"),
      color = axialColor
    ),
    horizontalCutData = CutData(
      type = CutType.CT_FRONTAL,
      data = data.planes[CT_FRONTAL_STRING]
        ?: error("CutsContainerStoreFactory: FRONTAL NOT FOUND IN DATA"),
      color = frontalColor
    ),
    researchType = data.type,
    availableCutsForChange = types
  )

private fun mgRcc(data: ResearchDataModel, types: List<CutType>): Plane =
  Plane(
    type = CutType.MG_RCC,
    data = data.planes[MG_RCC_STRING]
      ?: error("CutsContainerStoreFactory: MG_RCC NOT FOUND IN DATA"),
    color = rcc_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun mgLcc(data: ResearchDataModel, types: List<CutType>): Plane =
  Plane(
    type = CutType.MG_LCC,
    data = data.planes[MG_LCC_STRING]
      ?: error("CutsContainerStoreFactory: MG_LCC NOT FOUND IN DATA"),
    color = lcc_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun mgRmlo(data: ResearchDataModel, types: List<CutType>): Plane =
  Plane(
    type = CutType.MG_RMLO,
    data = data.planes[MG_RMLO_STRING]
      ?: error("CutsContainerStoreFactory: MG_RMLO NOT FOUND IN DATA"),
    color = rmlo_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun mgLmlo(data: ResearchDataModel, types: List<CutType>): Plane =
  Plane(
    type = CutType.MG_LMLO,
    data = data.planes[MG_LMLO_STRING]
      ?: error("CutsContainerStoreFactory: MG_LMLO NOT FOUND IN DATA"),
    color = lmlo_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun dxGeneric(data: ResearchDataModel, types: List<CutType>): Plane =
  Plane(
    type = CutType.DX_GENERIC,
    data = data.planes[DX_GENERIC_STRING]!!,
    color = generic_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun dxPosteroAnterior(data: ResearchDataModel, types: List<CutType>): Plane =
  Plane(
    type = CutType.DX_POSTERO_ANTERIOR,
    data = data.planes[DX_POSTERO_ANTERIOR_STRING]!!,
    color = postero_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = listOf()
  )

private fun dxLeftLateral(data: ResearchDataModel, types: List<CutType>): Plane =
  Plane(
    type = CutType.DX_LEFT_LATERAL,
    data = data.planes[DX_LEFT_LATERAL_STRING]!!,
    color = left_lateral_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = listOf()
  )

private fun dxRightLateral(data: ResearchDataModel, types: List<CutType>): Plane =
  Plane(
    type = CutType.DX_RIGHT_LATERAL,
    data = data.planes[DX_RIGHT_LATERAL_STRING]!!,
    color = right_lateral_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = listOf()
  )

private fun ct0(data: ResearchDataModel, types: List<CutType>): Plane =
  Plane(
    type = CutType.CT_0,
    data = data.planes[CT_0_STRING]!!,
    color = axialColor,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun ct1(data: ResearchDataModel, types: List<CutType>): Plane =
  Plane(
    type = CutType.CT_1,
    data = data.planes[CT_1_STRING]!!,
    color = frontalColor,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun ct2(data: ResearchDataModel, types: List<CutType>): Plane =
  Plane(
    type = CutType.CT_2,
    data = data.planes[CT_2_STRING]!!,
    color = sagittalColor,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )
