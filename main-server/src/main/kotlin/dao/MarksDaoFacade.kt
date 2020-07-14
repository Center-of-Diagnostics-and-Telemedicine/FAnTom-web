package dao

import model.MarkData
import model.MarkDomain

interface MarksDaoFacade {
  suspend fun get(id: Int): MarkDomain?
  suspend fun getAll(userId: Int, researchId: Int): List<MarkDomain>
  suspend fun save(mark: MarkData, userrId: Int, researchhId: Int): Int
  suspend fun update(mark: MarkDomain)
  suspend fun delete(id: Int)
}
