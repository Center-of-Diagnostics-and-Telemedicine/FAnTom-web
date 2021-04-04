package useCases

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import model.*
import repository.ResearchRepository
import repository.repository.ExpertMarksRepository
import util.ExpertMark
import util.user

fun Route.saveExpertMark(
  repository: ExpertMarksRepository,
  researchRepository: ResearchRepository
) {

  post<ExpertMark> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(ExpertMarksResponse(error = ErrorModel(errorCode.value)))
    }

    val user = call.user
    val markModel = call.receive<ExpertMarkEntity>()
    val research = researchRepository.getResearch(it.researchId)

    if (research == null) {
      respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
      return@post
    }

    try {

      val mark = repository.create(markModel.toExpertMarkModel(), user.id, it.researchId)

      if (mark != null) {
        call.respond(ExpertMarksResponse(response = listOf(mark.toExpertMarkEntity())))
      } else {
        respondError(ErrorStringCode.CREATE_MARK_FAILED)
      }

    } catch (e: Exception) {
      application.log.error("Failed to createRoi", e)
      respondError(ErrorStringCode.CREATE_MARK_FAILED)
    }
  }
}