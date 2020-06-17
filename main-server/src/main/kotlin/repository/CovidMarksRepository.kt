package repository

import dao.CovidMarkDaoFacade
import model.MarkModel

interface CovidMarkRepository {
  suspend fun getMark(userId: Int, researchId: Int): MarkModel?
  suspend fun createMark(markModel: MarkModel)
  suspend fun updateMark(markModel: MarkModel)
  suspend fun deleteMark(userId: Int, researchId: Int)
}

class CovidCovidMarkRepositoryImpl(private val covidMarkDaoFacade: CovidMarkDaoFacade) :
  CovidMarkRepository {

  override suspend fun getMark(userId: Int, researchId: Int): MarkModel? {
    return covidMarkDaoFacade.getMark(userId, researchId)
  }

  override suspend fun createMark(markModel: MarkModel) {
    covidMarkDaoFacade.createMark(markModel)
  }

  override suspend fun updateMark(markModel: MarkModel) {
    checkMarkExistence(userId = markModel.userId, researchId = markModel.researchId)
    covidMarkDaoFacade.updateMark(markModel)
  }

  override suspend fun deleteMark(userId: Int, researchId: Int) {
    checkMarkExistence(userId = userId, researchId = researchId)
    covidMarkDaoFacade.deleteMark(userId, researchId)

  }

  private suspend fun checkMarkExistence(userId: Int, researchId: Int) =
    covidMarkDaoFacade.getMark(userId = userId, researchId = researchId)
      ?: throw IllegalStateException("mark not found")

}
