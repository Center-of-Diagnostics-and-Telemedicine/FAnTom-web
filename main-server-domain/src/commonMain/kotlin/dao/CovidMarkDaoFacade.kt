package dao

import model.CovidMarkModel

interface CovidMarkDaoFacade {
  suspend fun getMark(userId: Int, researchId: Int): CovidMarkModel?
  suspend fun createMark(covidMark: CovidMarkModel)
  suspend fun deleteMark(researchId: Int, userId: Int)
  suspend fun updateMark(covidMark: CovidMarkModel)
}
