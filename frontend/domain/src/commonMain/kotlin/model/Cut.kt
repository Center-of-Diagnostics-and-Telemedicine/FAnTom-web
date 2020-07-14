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
        MPRPointPosition(
          x = dicomX,
          y = dicomY,
          z = sliceNumber.toDouble()
        )
      }
      CutType.CT_FRONTAL -> {
        val verticalRatio = verticalCutData!!.data.n_images.toDouble() / data.screen_size_v
        val horizontalRatio = horizontalCutData!!.data.n_images.toDouble() / data.screen_size_h
        MPRPointPosition(
          x = dicomX * horizontalRatio,
          y = sliceNumber.toDouble(),
          z = dicomY * verticalRatio
        )
      }
      CutType.CT_SAGITTAL -> {
        val verticalRatio = verticalCutData!!.data.n_images.toDouble() / data.screen_size_v
        val horizontalRatio = horizontalCutData!!.data.n_images.toDouble() / data.screen_size_h
        MPRPointPosition(
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
          radius = circle.dicomRadius,
          size = 0.0
        )
      }
      CutType.CT_FRONTAL -> {
        val verticalRatio = verticalCutData!!.data.n_images.toDouble() / data.screen_size_v
        val horizontalRatio = horizontalCutData!!.data.n_images.toDouble() / data.screen_size_h
        MarkData(
          x = (circle.dicomCenterX * horizontalRatio),
          y = sliceNumber.toDouble(),
          z = (circle.dicomCenterY * verticalRatio),
          radius = circle.dicomRadius,
          size = 0.0
        )
      }
      CutType.CT_SAGITTAL -> {
        val verticalRatio = verticalCutData!!.data.n_images.toDouble() / data.screen_size_v
        val horizontalRatio = horizontalCutData!!.data.n_images.toDouble() / data.screen_size_h
        MarkData(
          x = sliceNumber.toDouble(),
          y = (circle.dicomCenterX * horizontalRatio),
          z = (circle.dicomCenterY * verticalRatio),
          radius = circle.dicomRadius,
          size = 0.0
        )
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
}

fun Cut.getSliceNumberByMark(mark: MarkDomain): Int? {
  return when (type) {
    CutType.EMPTY -> null
    CutType.CT_AXIAL -> mark.markData.z.toInt()
    CutType.CT_FRONTAL -> mark.markData.y.toInt()
    CutType.CT_SAGITTAL -> mark.markData.x.toInt()
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
