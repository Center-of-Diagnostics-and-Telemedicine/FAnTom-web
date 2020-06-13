package repository

import model.Mark
import model.MarkToSave

interface MarksRepository {
  suspend fun getMarks(): List<Mark>
  suspend fun saveMark(markToSave: MarkToSave)
  suspend fun updateMark(mark: Mark)
  suspend fun deleteMark(id: Int)
}
