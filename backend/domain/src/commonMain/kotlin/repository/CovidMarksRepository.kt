package repository.repository

import model.CovidMarkModel

interface CovidMarksRepository {
  suspend fun getMark(userId: Int, researchId: Int): CovidMarkModel?
  suspend fun createMark(covidMark: CovidMarkModel)
  suspend fun updateMark(covidMark: CovidMarkModel)
  suspend fun deleteMark(userId: Int, researchId: Int)
}