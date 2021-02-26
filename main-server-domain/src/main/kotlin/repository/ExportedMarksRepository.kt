package repository.repository

import model.ExportedMarkModel

interface ExportedMarksRepository {
  suspend fun get(id: Int): ExportedMarkModel?
  suspend fun getAll(userId: Int, researchId: Int): List<ExportedMarkModel>
  suspend fun create(mark: ExportedMarkModel, userId: Int, researchId: Int): ExportedMarkModel?
  suspend fun update(mark: ExportedMarkModel)
  suspend fun delete(id: Int)
}