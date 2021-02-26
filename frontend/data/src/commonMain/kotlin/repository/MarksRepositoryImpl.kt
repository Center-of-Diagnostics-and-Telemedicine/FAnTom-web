package repository

import model.*
import model.ResearchApiExceptions.*

class MarksRepositoryImpl(
  val remote: MarksRemote,
  override val token: suspend () -> String
) : MarksRepository {

  override suspend fun getMarks(researchId: Int): List<MarkEntity> {
    val response = remote.getAll(token(), researchId)
    return when {
      response.response != null -> response.response!!.list
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw MarksFetchException
    }
  }

  override suspend fun saveMark(markToSave: MarkData, researchId: Int): MarkEntity {
    val response = remote.save(markToSave, researchId, token())
    return when {
      response.response != null -> response.response!!.mark
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw MarkCreateException
    }
  }

  override suspend fun updateMark(mark: MarkEntity, researchId: Int) {
    val response = remote.update(mark, researchId, token())
    when {
      response.response != null -> response.response!!
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw MarkUpdateException
    }
  }

  override suspend fun deleteMark(id: Int, researchId: Int) {
    val response = remote.delete(id, researchId, token())
    when {
      response.response != null -> response.response!!
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw MarkDeleteException
    }
  }

  override suspend fun clean() {
  }

  private fun <T : Any> handleErrorResponse(response: ErrorModel): T {
    when (response.error) {
      ErrorStringCode.RESEARCH_NOT_FOUND.value -> throw ResearchNotFoundException

      ErrorStringCode.GET_MARKS_FAILED.value -> throw MarksFetchException
      ErrorStringCode.CREATE_MARK_FAILED.value -> throw MarkCreateException
      ErrorStringCode.UPDATE_MARK_FAILED.value -> throw MarkUpdateException

      ErrorStringCode.SESSION_CLOSE_FAILED.value -> throw CloseSessionException
      ErrorStringCode.SESSION_EXPIRED.value -> throw SessionExpiredException
      else -> throw Exception(BASE_ERROR)
    }
  }

}
