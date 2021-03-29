package useCase

import model.CT_RESEARCH_CATEGORY
import model.CT_RESEARCH_MODALITY
import model.IdsModel
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
      val studyInstanceUid = researchData.studyInstanceUid
      val researchModelToSave = researchModel(
        accessionNumber = accessionNumber,
        studyID = studyID,
        studyInstanceUid = studyInstanceUid
      )
      researchRepository.createResearch(researchModelToSave)
      researchRepository.getResearchByAccessionNumber(accessionNumber)!!
    }
  }
}

private fun researchModel(
  accessionNumber: String,
  studyID: String,
  studyInstanceUid: String,
) = ResearchModel(
  id = -1,
  accessionNumber = accessionNumber,
  studyInstanceUID = studyInstanceUid,
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