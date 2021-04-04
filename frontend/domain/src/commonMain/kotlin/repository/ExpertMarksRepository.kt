package repository

import model.ExpertMarkEntity

interface ExpertMarksRepository {

  val token: suspend () -> String

  suspend fun getMarks(researchId: Int): List<ExpertMarkEntity>
  suspend fun saveMark(markToSave: ExpertMarkEntity, researchId: Int): ExpertMarkEntity
  suspend fun updateMark(markToSave: ExpertMarkEntity, researchId: Int)

}