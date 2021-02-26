package useCase

import IdsModel
import model.CT_RESEARCH_CATEGORY
import model.CT_RESEARCH_MODALITY
import model.ResearchModel
import repository.ResearchRepositoryImpl

suspend fun createResearch(
  researchData: IdsModel,
  researchRepository: ResearchRepositoryImpl
): ResearchModel {
  val accessionNumber = researchData.accessionNumber
  val existingResearch = researchRepository.getResearchByAccessionNumber(accessionNumber)
  return when {
    existingResearch != null -> existingResearch
    else -> {
      val studyID = researchData.studyId
      val researchModelToSave = researchModel(accessionNumber, studyID)
      researchRepository.createResearch(researchModelToSave)
      researchRepository
        .getResearchByAccessionNumber(accessionNumber)!!
    }
  }
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