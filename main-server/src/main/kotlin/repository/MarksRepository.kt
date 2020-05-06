package repository

import dao.MarkDaoFacade
import model.MarkModel

interface MarkRepository {
  suspend fun getMark(userId: Int, researchId: Int): MarkModel?
  suspend fun createMark(markModel: MarkModel)
  suspend fun updateMark(markModel: MarkModel)
  suspend fun deleteMark(userId: Int, researchId: Int)
}

class MarkRepositoryImpl(private val MarkDaoFacade: MarkDaoFacade) :
  MarkRepository {

  override suspend fun getMark(userId: Int, researchId: Int): MarkModel? {
    return MarkDaoFacade.getMark(userId, researchId)
  }

  override suspend fun createMark(markModel: MarkModel) {
    checkMarkExistence(userId = markModel.userId, researchId = markModel.researchId)
    MarkDaoFacade.createMark(markModel)
  }

  override suspend fun updateMark(markModel: MarkModel) {
    checkMarkExistence(userId = markModel.userId, researchId = markModel.researchId)
    MarkDaoFacade.updateMark(markModel)
  }

  override suspend fun deleteMark(userId: Int, researchId: Int) {
    checkMarkExistence(userId = userId, researchId = researchId)
    MarkDaoFacade.deleteMark(userId, researchId)

  }

  private suspend fun checkMarkExistence(userId: Int, researchId: Int) =
    MarkDaoFacade.getMark(userId = userId, researchId = researchId)
      ?: throw IllegalStateException("mark not found")

}