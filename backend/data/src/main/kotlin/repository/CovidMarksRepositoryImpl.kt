package repository

import repository.dao.CovidMarkDaoFacade
import model.CovidMarkModel
import repository.repository.CovidMarksRepository

class CovidMarksRepositoryImpl(private val covidMarkDaoFacade: CovidMarkDaoFacade) :
  CovidMarksRepository {

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