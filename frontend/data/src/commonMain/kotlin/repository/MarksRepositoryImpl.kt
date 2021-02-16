package repository

import model.*

class MarksRepositoryImpl(
  val remote: MarksRemote,
  override val token: suspend () -> String
) : MarksRepository {

  override suspend fun getMarks(researchId: Int): List<MarkEntity> {
    val response = remote.getAll(token(), researchId)
    return when {
      response.response != null -> response.response!!.list
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw ResearchApiExceptions.ResearchListFetchException
    }
  }

  override suspend fun saveMark(markToSave: MarkData, researchId: Int): MarkEntity {
    val response = remote.save(markToSave, researchId, token())
    return when {
      response.response != null -> response.response!!.mark
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw ResearchApiExceptions.ResearchListFetchException
    }
  }

  override suspend fun updateMark(mark: MarkEntity, researchId: Int) {
    val response = remote.update(mark, researchId, token())
    when {
      response.response != null -> response.response!!
      response.error != null -> handleErrorResponse(response.error!!)
    }
  }

  override suspend fun deleteMark(id: Int, researchId: Int) {
    val response = remote.delete(id, researchId, token())
    when {
      response.response != null -> response.response!!
      response.error != null -> handleErrorResponse(response.error!!)
    }
  }

  override suspend fun clean() {
  }

  private fun <T : Any> handleErrorResponse(response: ErrorModel): T {
    when (response.error) {
      ErrorStringCode.RESEARCH_NOT_FOUND.value -> throw ResearchApiExceptions.ResearchNotFoundException

      ErrorStringCode.GET_MARKS_FAILED.value -> throw ResearchApiExceptions.MarksFetchException
      ErrorStringCode.CREATE_MARK_FAILED.value -> throw ResearchApiExceptions.MarkCreateException
      ErrorStringCode.UPDATE_MARK_FAILED.value -> throw ResearchApiExceptions.MarkUpdateException

      ErrorStringCode.SESSION_CLOSE_FAILED.value -> throw ResearchApiExceptions.CloseSessionException
      ErrorStringCode.SESSION_EXPIRED.value -> throw ResearchApiExceptions.SessionExpiredException
      else -> throw Exception(BASE_ERROR)
    }
  }

}
