package model

import model.init.ResearchInitModel
import model.init.SeriesModel

data class ResearchDataModel(
  val series: Map<String, SeriesModel>,
  val type: ResearchType,
  val researchId: Int = -1,
  val markTypes: Map<String, MarkTypeEntity>
)

fun ResearchDataModel.defaultSeries(): SeriesModel =
  when(this.type){
    ResearchType.CT -> series[CT_DEFAULT_SERIES_STRING]!!
    ResearchType.MG -> series[MG_DEFAULT_SERIES_STRING]!!
    ResearchType.DX -> series[DX_DEFAULT_SERIES_STRING]!!
  }

fun ResearchInitModel.toResearchData(researchId: Int): ResearchDataModel =
  when {
    CT != null -> ResearchDataModel(
      series = this.CT!!,
      type = ResearchType.CT,
      researchId = researchId,
      markTypes = dictionary ?: mapOf()
    )
    MG != null -> ResearchDataModel(
      series = this.MG!!,
      type = ResearchType.MG,
      researchId = researchId,
      markTypes = dictionary ?: mapOf()
    )
    DX != null -> ResearchDataModel(
      series = this.DX!!,
      type = ResearchType.DX,
      researchId = researchId,
      markTypes = dictionary ?: mapOf()
    )
    else -> throw NotImplementedError("Modality not implemented")
  }
