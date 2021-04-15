package repository

import dao.ResearchDaoFacade
import model.ResearchModel

class ResearchRepositoryImpl(
  private val researchDaoFacade: ResearchDaoFacade
) : ResearchRepository {

  override suspend fun getAll(): List<ResearchModel> {
    return researchDaoFacade.getAll()
  }

  override suspend fun getResearch(researchId: Int): ResearchModel? {
    return researchDaoFacade.getResearchById(researchId)
  }

  override suspend fun getResearchByAccessionNumber(accessionNumber: String): ResearchModel? {
    return researchDaoFacade.getResearchByAccessionNumber(accessionNumber)
  }

  override suspend fun createResearch(researchModel: ResearchModel) {
    val existingResearch = researchDaoFacade.getResearchByAccessionNumber(researchModel.accessionNumber)
    if (existingResearch != null) {
      throw IllegalStateException("research exists")
    }
    researchDaoFacade.createResearch(researchModel)
  }

  override suspend fun updateResearch(researchModel: ResearchModel) {
    checkResearchExistence(researchModel.id)
    researchDaoFacade.updateResearch(researchModel)
  }

  override suspend fun deleteResearch(researchId: Int)  {
    checkResearchExistence(researchId)
    researchDaoFacade.deleteResearch(researchId)
  }

  private suspend fun checkResearchExistence(researchId: Int) =
    researchDaoFacade.getResearchById(researchId)
      ?: throw IllegalStateException("research not found")

}