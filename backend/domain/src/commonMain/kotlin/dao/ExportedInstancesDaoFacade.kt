package repository.dao

import model.ExportedRoiModel

interface ExportedRoisDaoFacade {
  suspend fun get(id: Int): ExportedRoiModel?
  suspend fun getAll(userId: Int, researchId: Int): List<ExportedRoiModel>
  suspend fun save(mark: ExportedRoiModel,researchhId: Int): Int
  suspend fun update(mark: ExportedRoiModel)
  suspend fun delete(id: Int)
}