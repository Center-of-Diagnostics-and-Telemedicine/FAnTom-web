package useCases

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import model.*
import repository.ResearchRepository
import repository.repository.ExportedRoisRepository
import util.CreateRoi
import util.user

fun Route.saveRoi(
  repository: ExportedRoisRepository,
  researchRepository: ResearchRepository
) {

  post<CreateRoi> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(ExpertRoisResponse(error = ErrorModel(errorCode.value)))
    }

    val user = call.user
    val markModel = call.receive<ExpertRoiEntity>()
    val research = researchRepository.getResearch(it.id)

    if (research == null) {
      respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
      return@post
    }

    try {

      val mark = repository.create(markModel.toExportedRoiModel(), it.id)

      if (mark != null) {
        call.respond(ExpertRoisResponse(response = listOf(mark.toExportedRoiEntity())))
      } else {
        respondError(ErrorStringCode.CREATE_MARK_FAILED)
      }

    } catch (e: Exception) {
      application.log.error("Failed to createRoi", e)
      respondError(ErrorStringCode.CREATE_MARK_FAILED)
    }
  }
}