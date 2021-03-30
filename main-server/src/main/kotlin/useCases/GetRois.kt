package useCases

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import model.*
import repository.ResearchRepository
import repository.repository.ExportedRoisRepository
import util.RoisMarks
import util.user

fun Route.getRois(
  exportedRoisRepository: ExportedRoisRepository,
  researchRepository: ResearchRepository
) {

  get<RoisMarks> { it ->
    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(ExpertRoisResponse(error = ErrorModel(errorCode.value)))
    }

    val user = call.user
    val research = researchRepository.getResearch(it.id)

    if (research == null) {
      respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
      return@get
    }

    try {
      val rois = exportedRoisRepository
        .getAll(user.id, it.id)
        .map(ExportedRoiModel::toExportedRoiEntity)

      when (rois) {
        null -> call.respond(ExpertRoisResponse(response = null))
        else -> call.respond(ExpertRoisResponse(response = rois))
      }

    } catch (e: Exception) {
      application.log.error("Failed to get Rois", e)
      respondError(ErrorStringCode.GET_MARKS_FAILED)
    }
  }

}