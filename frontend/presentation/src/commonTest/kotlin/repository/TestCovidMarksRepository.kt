package repository

import model.CovidMarkEntity
import testCovidMarkEntity
import testToken

class TestCovidMarksRepository : CovidMarksRepository {

  override val token: suspend () -> String = { testToken }

  override suspend fun getMark(researchId: Int): CovidMarkEntity {
    return testCovidMarkEntity
  }

  override suspend fun saveMark(markToSave: CovidMarkEntity, researchId: Int) {
  }

}