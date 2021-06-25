package repository.dao

import model.ResearchModel

interface ResearchDaoFacade {
  suspend fun getResearchById(researchId: Int): ResearchModel?
  suspend fun getResearchByAccessionNumber(accessionNumber: String): ResearchModel?
  suspend fun createResearch(researchModel: ResearchModel)
  suspend fun deleteResearch(researchId: Int)
  suspend fun updateResearch(researchModel: ResearchModel)
  suspend fun getAll(): List<ResearchModel>
}