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
data class ResearchData(
  val modalities: Map<Int, PlaneModel>,
  val type: ResearchType,
  val researchId: Int = -1,
  val reversed: Boolean,
  val markTypes: Map<String, MarkTypeEntity>,
  val doseReport: Boolean = false
)

fun initialResearchSlicesSizesData(): ResearchSlicesSizesData {
  return ResearchSlicesSizesData(
    axial = initialSlicesSizeData(),
    frontal = initialSlicesSizeData(),
    sagittal = initialSlicesSizeData(),
    pixelLength = .0
  )
}

fun ResearchInitModelNew.toResearchSlicesSizesData(doseReport: Boolean): ResearchData {
  return when {
    CT != null -> {
      if (doseReport) {
        println(dictionary)
        val modalities = mutableMapOf<Int, PlaneModel>()
        CT.CT0?.copy(reversed = CT.reversed)?.let { modalities[SLICE_TYPE_CT_0] = it }
        CT.CT1?.copy(reversed = CT.reversed)?.let { modalities[SLICE_TYPE_CT_1] = it }
        CT.CT2?.copy(reversed = CT.reversed)?.let { modalities[SLICE_TYPE_CT_2] = it }
        println(modalities.size)
        ResearchData(
          modalities = modalities,
          reversed = CT.reversed,
          type = ResearchType.MG,
          markTypes = dictionary!!,
          doseReport = true
        )
      } else {
        val modalities = mapOf(
          SLICE_TYPE_CT_AXIAL to CT.ct_axial!!.copy(reversed = CT.reversed),
          SLICE_TYPE_CT_FRONTAL to CT.ct_frontal!!.copy(reversed = CT.reversed),
          SLICE_TYPE_CT_SAGITTAL to CT.ct_sagittal!!.copy(reversed = CT.reversed),
        )
        ResearchData(
          modalities = modalities,
          reversed = CT.reversed,
          type = ResearchType.CT,
          markTypes = dictionary!!
        )
      }
    }
    MG != null -> {
      val modalities = mapOf(
        SLICE_TYPE_MG_RCC to MG.mg_rcc.copy(reversed = MG.reversed),
        SLICE_TYPE_MG_LCC to MG.mg_lcc.copy(reversed = MG.reversed),
        SLICE_TYPE_MG_RMLO to MG.mg_rmlo.copy(reversed = MG.reversed),
        SLICE_TYPE_MG_LMLO to MG.mg_lmlo.copy(reversed = MG.reversed)
      )
      ResearchData(
        modalities = modalities,
        reversed = MG.reversed,
        type = ResearchType.MG,
        markTypes = dictionary!!
      )
    }
    DX != null -> {
      val modalities = mapOf(
        SLICE_TYPE_DX_GENERIC to DX.dx0.copy(reversed = DX.reversed),
//        SLICE_TYPE_DX_POSTERO_ANTERIOR to DX.dx_postero_anterior.copy(reversed = DX.reversed),
//        SLICE_TYPE_DX_LEFT_LATERAL to DX.dx_left_lateral.copy(reversed = DX.reversed),
//        SLICE_TYPE_DX_RIGHT_LATERAL to DX.dx_right_lateral.copy(reversed = DX.reversed)
      )
      ResearchData(
        modalities = modalities,
        reversed = DX.reversed,
        type = ResearchType.DX,
        markTypes = dictionary!!
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
  val modality: String,
  val category: String
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

fun Research.isPlanar(): Boolean =
  when (this.modality) {
    CT_RESEARCH_MODALITY -> false
    MG_RESEARCH_MODALITY -> true
    DX_RESEARCH_MODALITY -> true
    else -> false
  }
