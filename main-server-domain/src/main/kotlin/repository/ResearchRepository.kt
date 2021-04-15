package repository

import model.ResearchModel

interface ResearchRepository {
  suspend fun getResearch(researchId: Int): ResearchModel?
  suspend fun getResearchByAccessionNumber(accessionNumber: String): ResearchModel?
  suspend fun createResearch(researchModel: ResearchModel)
  suspend fun deleteResearch(researchId: Int)
  suspend fun updateResearch(researchModel: ResearchModel)
  suspend fun getAll(): List<ResearchModel>
}
