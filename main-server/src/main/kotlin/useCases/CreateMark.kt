package useCases

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import model.*
import repository.MarksRepository
import repository.ResearchRepository
import util.CreateMark
import util.user

fun Route.createMark(
  multiPlanarMarksRepository: MarksRepository,
  planarMarksRepository: MarksRepository,
  researchRepository: ResearchRepository
) {

  post<CreateMark> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(MarkResponse(error = ErrorModel(errorCode.value)))
    }

    val user = call.user
    val markModel = call.receive<MarkData>()
    val research = researchRepository.getResearch(it.id)

    if (research == null) {
      respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
      return@post
    }

    try {

      val mark = if (research.modality == CT_RESEARCH_MODALITY && research.category != DOSE_REPORT_RESEARCH_CATEGORY) {
        multiPlanarMarksRepository.create(markModel, user.id, it.id)
      } else {
        planarMarksRepository.create(markModel, user.id, it.id)
      }

      if (mark != null) {
        call.respond(MarkResponse(MarkDomainModel(mark)))
      } else {
        respondError(ErrorStringCode.CREATE_MARK_FAILED)
      }

    } catch (e: Exception) {
      application.log.error("Failed to createMark", e)
      respondError(ErrorStringCode.CREATE_MARK_FAILED)
    }
  }
}
