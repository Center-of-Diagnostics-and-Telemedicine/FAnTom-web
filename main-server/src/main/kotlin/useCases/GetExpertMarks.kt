package useCases

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import model.*
import repository.ResearchRepository
import repository.repository.ExpertMarksRepository
import util.ExpertMarks
import util.user

fun Route.getExpertMarks(
  repository: ExpertMarksRepository,
  researchRepository: ResearchRepository
) {

  get<ExpertMarks> {
    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(ExpertMarksResponse(error = ErrorModel(errorCode.value)))
    }

    val user = call.user
    val research = researchRepository.getResearch(it.id)

    if (research == null) {
      respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
      return@get
    }

    try {
      val entities = repository
        .getAll(user.id, it.id)
        .map(ExpertMarkModel::toExpertMarkEntity)

      when (entities) {
        null -> call.respond(ExpertMarksResponse(response = emptyList()))
        else -> call.respond(ExpertMarksResponse(response = entities))
      }

    } catch (e: Exception) {
      application.log.error("Failed to get marks", e)
      respondError(ErrorStringCode.GET_MARKS_FAILED)
    }
  }

}