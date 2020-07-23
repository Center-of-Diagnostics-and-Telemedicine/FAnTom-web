package repository

import model.MarkEntity

interface MarksLocal {
  suspend fun get(markId: Int): MarkEntity?
  suspend fun getAll(): List<MarkEntity>
  suspend fun create(mark: MarkEntity)
  suspend fun update(mark: MarkEntity)
  suspend fun saveList(marks: List<MarkEntity>)
  suspend fun delete(markId: Int)
}
