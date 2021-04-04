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

fun Route.updateExpertMark(
  repository: ExpertMarksRepository,
  researchRepository: ResearchRepository
) {

  patch<ExpertMark> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(BaseResponse(error = ErrorModel(errorCode.value)))
    }

    val user = call.user
    val markModel = call.receive<ExpertMarkEntity>()
    val research = researchRepository.getResearch(it.researchId)

    if (research == null) {
      respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
      return@patch
    }

    try {

      val mark = repository.update(markModel.toExpertMarkModel())

      if (mark != null) {
        call.respond(BaseResponse(OK))
      } else {
        respondError(ErrorStringCode.CREATE_MARK_FAILED)
      }

    } catch (e: Exception) {
      application.log.error("Failed to createRoi", e)
      respondError(ErrorStringCode.CREATE_MARK_FAILED)
    }
  }
}