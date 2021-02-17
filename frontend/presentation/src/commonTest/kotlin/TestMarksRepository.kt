import model.MarkData
import model.MarkEntity
import repository.MarksRepository

class TestMarksRepository : MarksRepository {

  override val token: suspend () -> String = { testToken }

  override suspend fun getMarks(researchId: Int): List<MarkEntity> {
    return testMarks
  }

  override suspend fun saveMark(markToSave: MarkData, researchId: Int): MarkEntity {
    return MarkEntity(testIntId, markToSave, testMarkType.typeId, "")
  }

  override suspend fun updateMark(mark: MarkEntity, researchId: Int) {
  }

  override suspend fun deleteMark(id: Int, researchId: Int) {
  }

  override suspend fun clean() {
  }
}