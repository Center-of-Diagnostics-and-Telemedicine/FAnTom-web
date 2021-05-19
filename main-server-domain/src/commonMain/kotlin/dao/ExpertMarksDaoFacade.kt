package dao

import model.ExpertMarkModel

interface ExpertMarksDaoFacade {
  suspend fun get(id: Int): ExpertMarkModel?
  suspend fun getAll(researchId: Int): List<ExpertMarkModel>
  suspend fun save(mark: ExpertMarkModel, userrId: Int, researchhId: Int): Int
  suspend fun update(mark: ExpertMarkModel)
  suspend fun delete(id: Int)
}