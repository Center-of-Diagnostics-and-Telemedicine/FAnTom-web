package useCases

import io.ktor.application.*
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import model.ApiResponse
import model.ErrorStringCode
import repository.ResearchRepository
import repository.SessionRepository
import util.CloseSession
import util.user

fun Route.closeSession(
  researchRepository: ResearchRepository,
  sessionRepository: SessionRepository
) {

  get<CloseSession> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(ApiResponse.ErrorResponse(errorCode.value))
    }

    val userId = call.user.id

    val research = researchRepository.getResearch(it.id)
    if (research == null) respondError(ErrorStringCode.RESEARCH_NOT_FOUND)

    val session = sessionRepository.getSession(userId)
    if (session == null) respondError(ErrorStringCode.SESSION_EXPIRED)

    try {
      sessionRepository.deleteSession(userId, research!!.accessionNumber)
    } catch (e: Exception) {
      application.log.error("Failed to close session", e)
      respondError(ErrorStringCode.SESSION_CLOSE_FAILED)
    }
  }
}