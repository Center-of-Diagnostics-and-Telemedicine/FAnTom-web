package repository.dao

import model.ExportedMarkModel

interface ExportedMarksDaoFacade {
  suspend fun get(id: Int): ExportedMarkModel?
  suspend fun getAll(userId: Int, researchId: Int): List<ExportedMarkModel>
  suspend fun save(mark: ExportedMarkModel, userrId: Int, researchhId: Int): Int
  suspend fun update(mark: ExportedMarkModel)
  suspend fun delete(id: Int)
}