package repository

import model.MarkData
import model.MarkDomain

interface MarksRepository {

  val token: suspend () -> String

  suspend fun getMarks(researchId: Int): List<MarkDomain>
  suspend fun saveMark(markToSave: MarkData, researchId: Int)
  suspend fun updateMark(mark: MarkDomain)
  suspend fun deleteMark(id: Int)
}
