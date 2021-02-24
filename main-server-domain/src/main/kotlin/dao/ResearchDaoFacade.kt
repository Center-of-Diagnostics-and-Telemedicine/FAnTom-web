package dao

import model.ResearchModel

interface ResearchDaoFacade {
  suspend fun getResearchById(researchId: Int): ResearchModel?
  suspend fun getResearchByAccessionName(accessionNumber: String): ResearchModel?
  suspend fun createResearch(researchModel: ResearchModel)
  suspend fun deleteResearch(researchId: Int)
  suspend fun updateResearch(researchModel: ResearchModel)
}