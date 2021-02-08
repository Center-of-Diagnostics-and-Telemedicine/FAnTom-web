package repository

import model.*

class CovidMarksRepositoryImpl(
  val remote: CovidMarksRemote,
  override val token: suspend () -> String
) : CovidMarksRepository {

  override suspend fun getMark(researchId: Int): CovidMarkEntity {
    val response = remote.getMark(token(), researchId)

    return when {
      response.response != null -> response.response!!
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw ResearchApiExceptions.ResearchListFetchException
    }
  }

  override suspend fun saveMark(markToSave: CovidMarkEntity, researchId: Int) {
    val response = remote.save(request = markToSave, researchId = researchId, token = token())
    if (response.error != null) {
      handleErrorResponse<Any>(response.error!!)
    }
  }

  private fun <T : Any> handleErrorResponse(response: ErrorModel): T {
    when (response.error) {
      ErrorStringCode.RESEARCH_NOT_FOUND.value -> throw ResearchApiExceptions.ResearchNotFoundException

      ErrorStringCode.CREATE_MARK_FAILED.value -> throw ResearchApiExceptions.MarkCreateException
      ErrorStringCode.UPDATE_MARK_FAILED.value -> throw ResearchApiExceptions.MarkUpdateException

      ErrorStringCode.SESSION_CLOSE_FAILED.value -> throw ResearchApiExceptions.CloseSessionException
      ErrorStringCode.SESSION_EXPIRED.value -> throw ResearchApiExceptions.SessionExpiredException
      else -> throw Exception(BASE_ERROR)
    }
  }
}