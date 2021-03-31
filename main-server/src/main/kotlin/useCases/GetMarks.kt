package useCases

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import model.*
import repository.MarksRepository
import repository.ResearchRepository
import util.GetMarks
import util.user

fun Route.getMarks(
  multiPlanarMarksRepository: MarksRepository,
  planarMarksRepository: MarksRepository,
  researchRepository: ResearchRepository
) {

  get<GetMarks> {
    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(MarksResponseNew(error = ErrorModel(errorCode.value)))
    }

    val user = call.user
    val research = researchRepository.getResearch(it.id)

    if (research == null) {
      respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
      return@get
    }

    try {

      val marks = if (research.modality == CT_RESEARCH_MODALITY && research.category != DOSE_REPORT_RESEARCH_CATEGORY) {
        multiPlanarMarksRepository.getAll(user.id, it.id)
      } else {
        planarMarksRepository.getAll(user.id, it.id)
      }

      call.respond(MarksResponseNew(MarksModel(marks)))

    } catch (e: Exception) {
      application.log.error("Failed to get marks", e)
      respondError(ErrorStringCode.GET_MARKS_FAILED)
    }
  }
}
