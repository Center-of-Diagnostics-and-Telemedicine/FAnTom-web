package useCase

import IdsModel
import model.*
import repository.ResearchRepositoryImpl

suspend fun createResearches(
  researchesData: List<IdsModel>,
  researchRepository: ResearchRepositoryImpl
): List<ResearchModel> {
  val researches = mutableListOf<ResearchModel>()

  researchesData.forEach { idModel ->

    val accessionNumber = idModel.accessionNumber
    val existingResearch = researchRepository.getResearchByAccessionNumber(accessionNumber)
    if (existingResearch != null) {
      researches.add(existingResearch)
    } else {
      val studyID = idModel.studyId
      val researchModelToSave = researchModel(accessionNumber, studyID)
      researchRepository.createResearch(researchModelToSave)
      researchRepository
        .getResearchByAccessionNumber(accessionNumber)
        ?.let { researches.add(it) }
    }
  }

  return researches
}

private fun researchModel(
  accessionNumber: String,
  studyID: String
) = ResearchModel(
  id = -1,
  accessionNumber = accessionNumber,
  studyInstanceUID = studyID,
  studyID = studyID,
  protocol = "",
  accessionNumberBase = accessionNumber,
  accessionNumberLastDigit = accessionNumber.last().toString(),
  jsonName = accessionNumber + "_" + studyID,
  doctor1 = "",
  doctor2 = "",
  posInBlock = "",
  modality = CT_RESEARCH_MODALITY,
  category = CT_RESEARCH_CATEGORY,
)