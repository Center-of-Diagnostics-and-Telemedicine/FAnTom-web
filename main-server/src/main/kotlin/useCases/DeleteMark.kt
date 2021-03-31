package useCases

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import model.*
import repository.MarksRepository
import repository.ResearchRepository
import util.DeleteMark

fun Route.deleteMark(
  multiPlanarMarksRepository: MarksRepository,
  planarMarksRepository: MarksRepository,
  researchRepository: ResearchRepository
) {

  delete<DeleteMark> {
    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(BaseResponse(error = ErrorModel(errorCode.value)))
    }

    val research = researchRepository.getResearch(it.researchId)

    if (research == null) {
      respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
      return@delete
    }

    try {

      if (research.modality == CT_RESEARCH_MODALITY && research.category != DOSE_REPORT_RESEARCH_CATEGORY) {
        multiPlanarMarksRepository.delete(it.markId)
      } else {
        planarMarksRepository.delete(it.markId)
      }

      call.respond(BaseResponse(OK()))
    } catch (e: Exception) {
      application.log.error("Failed to delete mark", e)
      respondError(ErrorStringCode.DELETE_MARK_FAILED)
    }
  }
}
