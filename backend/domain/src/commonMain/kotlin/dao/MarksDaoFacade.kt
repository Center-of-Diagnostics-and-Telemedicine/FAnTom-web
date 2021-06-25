package repository.dao

import model.MarkData
import model.MarkEntity

interface MarksDaoFacade {
  suspend fun get(id: Int): MarkEntity?
  suspend fun getAll(userId: Int, researchId: Int): List<MarkEntity>
  suspend fun save(mark: MarkData, userrId: Int, researchhId: Int): Int
  suspend fun update(mark: MarkEntity)
  suspend fun delete(id: Int)
}
