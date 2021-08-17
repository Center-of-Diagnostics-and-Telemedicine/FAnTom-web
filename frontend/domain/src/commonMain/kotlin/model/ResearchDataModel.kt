package model

import model.init.ResearchInitModel

data class ResearchDataModel(
  val planes: Map<String, PlaneModel>,
  val type: ResearchType,
  val researchId: Int = -1,
  val reversed: Boolean,
  val markTypes: Map<String, MarkTypeEntity>
)

fun ResearchInitModel.toResearchData(researchId: Int): ResearchDataModel =
  when {
    CT != null -> ResearchDataModel(
      planes = CT!!.planes,
      type = ResearchType.CT,
      researchId = researchId,
      reversed = CT!!.reversed,
      markTypes = dictionary ?: mapOf()
    )
    MG != null -> ResearchDataModel(
      planes = this.MG!!.planes,
      type = ResearchType.MG,
      researchId = researchId,
      reversed = MG!!.reversed,
      markTypes = dictionary ?: mapOf()
    )
    DX != null -> ResearchDataModel(
      planes = this.DX!!.planes,
      type = ResearchType.DX,
      researchId = researchId,
      reversed = DX!!.reversed,
      markTypes = dictionary ?: mapOf()
    )
    else -> throw NotImplementedError("Modality not implemented")
  }

fun ResearchDataModel.series(): Map<String, SeriesModel> =
  when (type) {
    ResearchType.CT -> ctSeries()
    ResearchType.MG -> mgSeries()
    ResearchType.DX -> dxSeries()
  }

fun ResearchDataModel.initialSeries(): SeriesModel =
  when (type) {
    ResearchType.CT -> initialCtSeries()
    ResearchType.MG -> initialMgSeries()
    ResearchType.DX -> initialDxSeries()
  }

private fun initialDxSeries(): SeriesModel {
  return SeriesModel(DX_DEFAULT_SERIES_STRING, dxDefaultCutTypes)
}

fun initialMgSeries(): SeriesModel {
  return SeriesModel(MG_DEFAULT_SERIES_STRING, mgDefaultCutTypes)
}

private fun initialCtSeries(): SeriesModel {
  return SeriesModel(CT_DEFAULT_SERIES_STRING, ctDefaultCutTypes)
}

private fun ResearchDataModel.ctSeries(): Map<String, SeriesModel> {
  val map = mutableMapOf<String, SeriesModel>()
  //add default series
  map[CT_DEFAULT_SERIES_STRING] = SeriesModel(CT_DEFAULT_SERIES_STRING, ctDefaultCutTypes)
  //add other series
  val otherPlanes = planes.filterKeys { key -> inDefaultCTKeys(key).not() }
  otherPlanes.forEach { map[it.key] = SeriesModel(it.key, listOf(CutType.CT_UNKNOWN)) }
  return map
}

private fun ResearchDataModel.mgSeries(): Map<String, SeriesModel> {
  val map = mutableMapOf<String, SeriesModel>()
  //add default series
  map[MG_DEFAULT_SERIES_STRING] = SeriesModel(MG_DEFAULT_SERIES_STRING, mgDefaultCutTypes)
  //add other series
  val otherPlanes = planes.filterKeys { key -> inDefaultMGKeys(key).not() }
  otherPlanes.forEach { map[it.key] = SeriesModel(it.key, listOf(CutType.MG_UNKNOWN)) }
  return map
}

private fun ResearchDataModel.dxSeries(): Map<String, SeriesModel> {
  val map = mutableMapOf<String, SeriesModel>()
  //add default series
  map[DX_DEFAULT_SERIES_STRING] = SeriesModel(DX_DEFAULT_SERIES_STRING, dxDefaultCutTypes)
  //add other series
  val otherPlanes = planes.filterKeys { key -> inDefaultDXKeys(key).not() }
  otherPlanes.forEach { map[it.key] = SeriesModel(it.key, listOf(CutType.DX_UNKNOWN)) }
  return map
}

private fun inDefaultCTKeys(key: String) = ctDefaultStringTypes.firstOrNull { it == key } != null
private fun inDefaultMGKeys(key: String) = mgDefaultStringTypes.firstOrNull { it == key } != null
private fun inDefaultDXKeys(key: String) = dxDefaultStringTypes.firstOrNull { it == key } != null
