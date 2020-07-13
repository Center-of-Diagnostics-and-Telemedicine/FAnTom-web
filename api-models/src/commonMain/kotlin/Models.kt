package model

import kotlinx.serialization.Serializable


@Serializable
data class ResearchSlicesSizesData(
  val axial: SliceSizeData,
  val frontal: SliceSizeData,
  val sagittal: SliceSizeData,
  val pixelLength: Double,
  val researchId: Int = -1,
  val reversed: Boolean = false
)

@Serializable
data class ResearchSlicesSizesDataNew(
  val modalities: Map<Int, ModalityModel>,
  val type: ResearchType,
  val researchId: Int = -1,
  val reversed: Boolean
)

fun initialResearchSlicesSizesData(): ResearchSlicesSizesData {
  return ResearchSlicesSizesData(
    axial = initialSlicesSizeData(),
    frontal = initialSlicesSizeData(),
    sagittal = initialSlicesSizeData(),
    pixelLength = .0
  )
}

fun ResearchInitModel.toResearchSlicesSizesData(): ResearchSlicesSizesData {
  return ResearchSlicesSizesData(
    axial = SliceSizeData(
      maxFramesSize = axialReal,
      height = frontalInterpolated,
      pixelLength = pixelLength,
      reversed = reversed
    ),
    frontal = SliceSizeData(
      maxFramesSize = frontalReal,
      height = axialInterpolated,
      pixelLength = pixelLength,
      reversed = reversed
    ),
    sagittal = SliceSizeData(
      maxFramesSize = sagittalReal,
      height = axialInterpolated,
      pixelLength = pixelLength,
      reversed = reversed
    ),
    pixelLength = pixelLength,
    reversed = reversed
  )
}

fun ResearchInitModelNew.toResearchSlicesSizesData(): ResearchSlicesSizesDataNew {
  return when {
    CT != null -> {
      val modalities = mapOf(
        SLICE_TYPE_CT_AXIAL to CT.ct_axial.copy(reversed = CT.reversed),
        SLICE_TYPE_CT_FRONTAL to CT.ct_frontal.copy(reversed = CT.reversed),
        SLICE_TYPE_CT_SAGITTAL to CT.ct_sagittal.copy(reversed = CT.reversed),
      )
      ResearchSlicesSizesDataNew(
        modalities = modalities,
        reversed = CT.reversed,
        type = ResearchType.CT
      )
    }
    MG != null -> {
      val modalities = mapOf(
        SLICE_TYPE_MG_RCC to MG.mg_rcc.copy(reversed = MG.reversed),
        SLICE_TYPE_MG_LCC to MG.mg_lcc.copy(reversed = MG.reversed),
        SLICE_TYPE_MG_RMLO to MG.mg_rmlo.copy(reversed = MG.reversed),
        SLICE_TYPE_MG_LMLO to MG.mg_lmlo.copy(reversed = MG.reversed)
      )
      ResearchSlicesSizesDataNew(
        modalities = modalities,
        reversed = MG.reversed,
        type = ResearchType.MG
      )
    }
    DX != null -> {
      val modalities = mapOf(
        SLICE_TYPE_DX_GENERIC to DX.dx_generic.copy(reversed = DX.reversed),
        SLICE_TYPE_DX_POSTERO_ANTERIOR to DX.dx_postero_anterior.copy(reversed = DX.reversed),
        SLICE_TYPE_DX_LEFT_LATERAL to DX.dx_left_lateral.copy(reversed = DX.reversed),
        SLICE_TYPE_DX_RIGHT_LATERAL to DX.dx_right_lateral.copy(reversed = DX.reversed)
      )
      ResearchSlicesSizesDataNew(
        modalities = modalities,
        reversed = DX.reversed,
        type = ResearchType.DX
      )
    }
    else -> throw NotImplementedError("ResearchInitModelNew.toResearchSlicesSizesData failed")
  }
}

fun initialSlicesSizeData(): SliceSizeData {
  return SliceSizeData(
    maxFramesSize = 0,
    height = 0,
    pixelLength = 0.0,
    reversed = false
  )
}

@Serializable
data class SliceSizeData(
  val maxFramesSize: Int,
  val height: Int,
  val pixelLength: Double,
  val reversed: Boolean
)

@Serializable
data class Research(
  val id: Int,
  val name: String,
  val seen: Boolean,
  val done: Boolean,
  val marked: Boolean,
  val modality: String
)

@Serializable
data class OK(val status: String = "ok")

@Serializable
data class MarksResponse(
  val marks: List<Mark>
)

@Serializable
data class MarkRequest(
  val mark: Mark,
  val researchId: Int
)

@Serializable
data class NewMarkRequest(
  val mark: AreaToSave,
  val researchId: Int
)

@Serializable
data class Mark(
  val x: Double,
  val y: Double,
  val z: Double,
  val areaType: AreaType,
  val radius: Double,
  val size: Double,
  val id: Int,
  val comment: String
)

@Serializable
data class AreaToSave(
  val x: Double,
  val y: Double,
  val z: Double,
  val radius: Double,
  val size: Double
)

@Serializable
data class AccessionNamesResponse(
  val accessionNames: List<String>
)


@Serializable
data class User(
  val id: Int,
  val name: String,
  val password: String
)
