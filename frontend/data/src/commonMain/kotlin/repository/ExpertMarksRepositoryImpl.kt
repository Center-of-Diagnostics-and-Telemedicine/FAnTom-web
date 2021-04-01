package repository

import model.*

class ExpertMarksRepositoryImpl(
  val remote: ExpertMarksRemote,
  override val token: suspend () -> String
) : ExpertMarksRepository {

  override suspend fun getMarks(researchId: Int): List<ExpertMarkEntity> {
    val response = remote.getMark(token(), researchId)

    return when {
      response.response != null -> response.response!!
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw ResearchApiExceptions.MarksFetchException
    }
  }

  override suspend fun saveMark(markToSave: ExpertMarkEntity, researchId: Int): ExpertMarkEntity {
    val response = remote.save(request = markToSave, researchId = researchId, token = token())
    return when {
      response.response != null -> response.response!!.first()
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw ResearchApiExceptions.MarkUpdateException
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