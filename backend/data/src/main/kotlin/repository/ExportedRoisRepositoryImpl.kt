package repository

import model.ExportedRoiModel
import repository.dao.ExportedRoisDaoFacade
import repository.repository.ExportedRoisRepository

class ExportedRoisRepositoryImpl(
  private val exportedMarksDao: ExportedRoisDaoFacade
) : ExportedRoisRepository {

  override suspend fun get(id: Int): ExportedRoiModel? {
    return exportedMarksDao.get(id)
  }

  override suspend fun getAll(userId: Int, researchId: Int): List<ExportedRoiModel> {
    return exportedMarksDao.getAll(userId, researchId)
  }

  override suspend fun create(
    mark: ExportedRoiModel,
    researchId: Int
  ): ExportedRoiModel? {
    return exportedMarksDao
      .save(mark, researchId)
      .let { id ->
        exportedMarksDao.get(id)
      }
  }

  override suspend fun update(mark: ExportedRoiModel) {
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