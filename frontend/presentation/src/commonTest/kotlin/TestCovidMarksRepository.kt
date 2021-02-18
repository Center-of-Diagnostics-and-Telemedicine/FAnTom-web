import model.CovidMarkEntity
import repository.CovidMarksRepository

class TestCovidMarksRepository : CovidMarksRepository {

  override val token: suspend () -> String = { testToken }

  override suspend fun getMark(researchId: Int): CovidMarkEntity {
    return testCovidMarkEntity
  }

  override suspend fun saveMark(markToSave: CovidMarkEntity, researchId: Int) {
  }

}