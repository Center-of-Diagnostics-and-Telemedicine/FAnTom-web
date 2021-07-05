package repository

import repository.dao.MarksDaoFacade
import model.MarkData
import model.MarkEntity

class MultiPlanarMarksRepositoryImpl(
  private val marksDaoFacade: MarksDaoFacade
) : MarksRepository {

  override suspend fun get(id: Int): MarkEntity? {
    return marksDaoFacade.get(id)
  }

  override suspend fun getAll(userId: Int, researchId: Int): List<MarkEntity> {
    return marksDaoFacade.getAll(userId, researchId)
  }

  override suspend fun create(mark: MarkData, userId: Int, researchId: Int): MarkEntity? {
    return marksDaoFacade
      .save(mark, userId, researchId)
      .let { id ->
        marksDaoFacade.get(id)
      }
  }

  override suspend fun update(mark: MarkEntity) {
    checkMarkExistence(mark.id)
    marksDaoFacade.update(mark)
  }

  override suspend fun delete(id: Int) {
    checkMarkExistence(id)
    marksDaoFacade.delete(id)
  }

  private suspend fun checkMarkExistence(id: Int) =
    marksDaoFacade.get(id)
      ?: throw IllegalStateException("mark not found")

}