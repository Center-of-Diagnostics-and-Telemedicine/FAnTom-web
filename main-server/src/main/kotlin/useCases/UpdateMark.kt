package useCases

import io.ktor.application.*
import io.ktor.locations.put
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.Route
import model.*
import repository.MarksRepository
import repository.ResearchRepository
import util.UpdateMark

fun Route.updateMark(
  multiPlanarMarksRepository: MarksRepository,
  planarMarksRepository: MarksRepository,
  researchRepository: ResearchRepository
) {

  put<UpdateMark> {
    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(BaseResponse(error = ErrorModel(errorCode.value)))
    }

    val mark = call.receive<MarkDomain>()
    val research = researchRepository.getResearch(it.id)

    if (research == null) {
      respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
      return@put
    }

    try {

      if (research.modality == CT_RESEARCH_TYPE) {
        multiPlanarMarksRepository.update(mark)
      } else {
        planarMarksRepository.update(mark)
      }

      call.respond(BaseResponse(OK()))
    } catch (e: Exception) {
      application.log.error("Failed to update mark", e)
      respondError(ErrorStringCode.UPDATE_MARK_FAILED)
    }
  }
}
