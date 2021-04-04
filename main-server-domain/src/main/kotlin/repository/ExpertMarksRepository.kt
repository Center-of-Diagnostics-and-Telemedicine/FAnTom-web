package repository.repository

import model.ExpertMarkModel

interface ExpertMarksRepository {
  suspend fun get(id: Int): ExpertMarkModel?
  suspend fun getByRoiId(roiId: Int): ExpertMarkModel?
  suspend fun getAll(userId: Int, researchId: Int): List<ExpertMarkModel>
  suspend fun create(mark: ExpertMarkModel, userId: Int, researchId: Int): ExpertMarkModel?
  suspend fun update(mark: ExpertMarkModel)
  suspend fun delete(id: Int)
}