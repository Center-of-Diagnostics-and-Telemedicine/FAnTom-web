package repository

import model.ExportedMarkModel
import repository.dao.ExportedMarksDaoFacade
import repository.repository.ExportedMarksRepository

class ExportedMarksRepositoryImpl(
  private val exportedMarksDao: ExportedMarksDaoFacade
) : ExportedMarksRepository {

  override suspend fun get(id: Int): ExportedMarkModel? {
    return exportedMarksDao.get(id)
  }

  override suspend fun getAll(userId: Int, researchId: Int): List<ExportedMarkModel> {
    return exportedMarksDao.getAll(userId, researchId)
  }

  override suspend fun create(
    mark: ExportedMarkModel,
    userId: Int,
    researchId: Int
  ): ExportedMarkModel? {
    return exportedMarksDao
      .save(mark, userId, researchId)
      .let { id ->
        exportedMarksDao.get(id)
      }
  }

  override suspend fun update(mark: ExportedMarkModel) {
    checkMarkExistence(mark.id)
    exportedMarksDao.update(mark)
  }

  override suspend fun delete(id: Int) {
    checkMarkExistence(id)
    exportedMarksDao.delete(id)
  }

  private suspend fun checkMarkExistence(id: Int) =
    exportedMarksDao.get(id)
      ?: throw IllegalStateException("mark not found")

}