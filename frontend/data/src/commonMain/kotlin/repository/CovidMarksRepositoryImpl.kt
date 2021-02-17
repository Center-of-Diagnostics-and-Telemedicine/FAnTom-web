package repository

import model.*
import model.ResearchApiExceptions.*

class CovidMarksRepositoryImpl(
  val remote: CovidMarksRemote,
  override val token: suspend () -> String
) : CovidMarksRepository {

  override suspend fun getMark(researchId: Int): CovidMarkEntity {
    val response = remote.getMark(token(), researchId)

    return when {
      response.response != null -> response.response!!
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw MarksFetchException
    }
  }

  override suspend fun saveMark(markToSave: CovidMarkEntity, researchId: Int) {
    val response = remote.save(request = markToSave, researchId = researchId, token = token())
    when {
      response.response != null -> response.response!!
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw MarkUpdateException
    }
  }

  private fun <T : Any> handleErrorResponse(response: ErrorModel): T {
    when (response.error) {
      ErrorStringCode.RESEARCH_NOT_FOUND.value -> throw ResearchNotFoundException

      ErrorStringCode.CREATE_MARK_FAILED.value -> throw MarkCreateException
      ErrorStringCode.UPDATE_MARK_FAILED.value -> throw MarkUpdateException

      ErrorStringCode.SESSION_CLOSE_FAILED.value -> throw CloseSessionException
      ErrorStringCode.SESSION_EXPIRED.value -> throw SessionExpiredException
      else -> throw Exception(BASE_ERROR)
    }
  }
}