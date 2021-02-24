package repository

import dao.CovidMarkDaoFacade
import model.CovidMarkModel

interface CovidMarkRepository {
  suspend fun getMark(userId: Int, researchId: Int): CovidMarkModel?
  suspend fun createMark(covidMark: CovidMarkModel)
  suspend fun updateMark(covidMark: CovidMarkModel)
  suspend fun deleteMark(userId: Int, researchId: Int)
}

class CovidCovidMarkRepositoryImpl(private val covidMarkDaoFacade: CovidMarkDaoFacade) :
  CovidMarkRepository {

  override suspend fun getMark(userId: Int, researchId: Int): CovidMarkModel? {
    return covidMarkDaoFacade.getMark(userId, researchId)
  }

  override suspend fun createMark(covidMark: CovidMarkModel) {
    covidMarkDaoFacade.createMark(covidMark)
  }

  override suspend fun updateMark(covidMark: CovidMarkModel) {
    checkMarkExistence(userId = covidMark.userId, researchId = covidMark.researchId)
    covidMarkDaoFacade.updateMark(covidMark)
  }

  override suspend fun deleteMark(userId: Int, researchId: Int) {
    checkMarkExistence(userId = userId, researchId = researchId)
    covidMarkDaoFacade.deleteMark(userId, researchId)

  }

  private suspend fun checkMarkExistence(userId: Int, researchId: Int) =
    covidMarkDaoFacade.getMark(userId = userId, researchId = researchId)
      ?: throw IllegalStateException("mark not found")

}
