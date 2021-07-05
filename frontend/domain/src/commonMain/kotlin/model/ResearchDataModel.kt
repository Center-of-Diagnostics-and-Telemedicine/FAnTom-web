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
