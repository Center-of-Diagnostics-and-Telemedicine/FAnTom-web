package repository

import model.*

class MarksRepositoryImpl(
  val local: MarksLocal,
  val remote: MarksRemote,
  override val token: suspend () -> String
) : MarksRepository {

  override suspend fun getMarks(researchId: Int): List<MarkEntity> {
    val cached = local.getAll()
    if (cached.isNotEmpty()) {
      return cached
    }
    val response = remote.getAll(token(), researchId)
    return when {
      response.response != null -> {
        val marks = response.response!!.list
        local.saveList(marks)
        marks
      }
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw ResearchApiExceptions.ResearchListFetchException
    }
  }

  override suspend fun saveMark(markToSave: MarkData, researchId: Int) {
    val response = remote.save(markToSave, researchId, token())
    when {
      response.response != null -> {
        val mark = response.response!!.mark
        local.create(mark)
      }
      response.error != null -> handleErrorResponse(response.error!!)
    }
  }

  override suspend fun updateMark(mark: MarkEntity, researchId: Int, localy: Boolean) {
    if (localy) {
      local.update(mark)
    } else {
      val response = remote.update(mark, researchId, token())
      when {
        response.response != null -> local.update(mark)
        response.error != null -> handleErrorResponse(response.error!!)
      }
    }
  }

  override suspend fun deleteMark(id: Int, researchId: Int) {
    val response = remote.delete(id, researchId, token())
    when {
      response.response != null -> local.delete(id)
      response.error != null -> handleErrorResponse(response.error!!)
    }
  }

  override suspend fun clean() {
    local.clean()
  }

  private fun <T : Any> handleErrorResponse(response: ErrorModel): T {
    when (response.error) {
      ErrorStringCode.RESEARCH_NOT_FOUND.value -> throw ResearchApiExceptions.ResearchNotFoundException

      ErrorStringCode.CREATE_MARK_FAILED.value -> throw ResearchApiExceptions.ConfirmCtTypeForResearchException

      ErrorStringCode.SESSION_CLOSE_FAILED.value -> throw ResearchApiExceptions.CloseSessionException
      ErrorStringCode.SESSION_EXPIRED.value -> throw ResearchApiExceptions.SessionExpiredException
      else -> throw Exception(BASE_ERROR)
    }
  }

}
