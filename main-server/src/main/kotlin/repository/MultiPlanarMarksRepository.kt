package repository

import dao.MarksDaoFacade
import model.MarkData
import model.MarkDomain

class MultiPlanarMarksRepositoryImpl(
  private val marksDaoFacade: MarksDaoFacade
) : MarksRepository {

  override suspend fun get(id: Int): MarkDomain? {
    return marksDaoFacade.get(id)
  }

  override suspend fun getAll(userId: Int, researchId: Int): List<MarkDomain> {
    return marksDaoFacade.getAll(userId, researchId)
  }

  override suspend fun create(mark: MarkData, userId: Int, researchId: Int): MarkDomain? {
    return marksDaoFacade
      .save(mark, userId, researchId)
      .let { id ->
        marksDaoFacade.get(id)
      }
  }

  override suspend fun update(mark: MarkDomain) {
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
