package repository

import model.MarkData
import model.MarkEntity

interface MarksRepository {

  val token: suspend () -> String

  suspend fun getMarks(researchId: Int): List<MarkEntity>
  suspend fun saveMark(markToSave: MarkData, researchId: Int)
  suspend fun updateMark(mark: MarkEntity, researchId: Int, localy: Boolean = false)
  suspend fun deleteMark(id: Int, researchId: Int)
}
