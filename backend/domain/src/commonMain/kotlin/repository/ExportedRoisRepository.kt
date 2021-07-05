package repository

import model.ExportedRoiModel

interface ExportedRoisRepository {
  suspend fun get(id: Int): ExportedRoiModel?
  suspend fun getAll(userId: Int, researchId: Int): List<ExportedRoiModel>
  suspend fun create(mark: ExportedRoiModel, researchId: Int): ExportedRoiModel?

  suspend fun update(mark: ExportedRoiModel)
  suspend fun delete(id: Int)
}