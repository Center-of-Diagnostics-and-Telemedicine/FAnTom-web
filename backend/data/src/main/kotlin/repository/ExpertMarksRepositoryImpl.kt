package repository

import model.ExpertMarkModel
import repository.dao.ExpertMarksDaoFacade
import repository.repository.ExpertMarksRepository

class ExpertMarksRepositoryImpl(
  private val expertMarksDao: ExpertMarksDaoFacade
) : ExpertMarksRepository {

  override suspend fun get(id: Int): ExpertMarkModel? {
    return expertMarksDao.get(id)
  }

  override suspend fun getAll(researchId: Int): List<ExpertMarkModel> {
    return expertMarksDao.getAll(researchId)
  }

  override suspend fun create(
    mark: ExpertMarkModel,
    userId: Int,
    researchId: Int
  ): ExpertMarkModel? {
    return expertMarksDao
      .save(mark, userId, researchId)
      .let { id ->
        expertMarksDao.get(id)
      }
  }

  override suspend fun update(mark: ExpertMarkModel) {
    checkMarkExistence(mark.id)
    expertMarksDao.update(mark)
  }

  override suspend fun delete(id: Int) {
    checkMarkExistence(id)
    expertMarksDao.delete(id)
  }

  private suspend fun checkMarkExistence(id: Int) =
    expertMarksDao.get(id)
      ?: throw IllegalStateException("mark not found")

}