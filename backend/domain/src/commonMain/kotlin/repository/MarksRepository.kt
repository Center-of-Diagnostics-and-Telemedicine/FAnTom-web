package repository.repository

import model.MarkData
import model.MarkEntity

interface MarksRepository {
  suspend fun get(id: Int): MarkEntity?
  suspend fun getAll(userId: Int, researchId: Int): List<MarkEntity>
  suspend fun create(mark: MarkData, userId: Int, researchId: Int): MarkEntity?
  suspend fun update(mark: MarkEntity)
  suspend fun delete(id: Int)
}