package repository

import model.MarkData
import model.MarkDomain

interface MarksRepository {
  suspend fun get(id: Int): MarkDomain?
  suspend fun getAll(userId: Int, researchId: Int): List<MarkDomain>
  suspend fun create(mark: MarkData, userId: Int, researchId: Int): MarkDomain?
  suspend fun update(mark: MarkDomain)
  suspend fun delete(id: Int)
}